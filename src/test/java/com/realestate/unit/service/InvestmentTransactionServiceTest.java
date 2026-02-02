package com.realestate.unit.service;

import com.realestate.exception.BadRequestException;
import com.realestate.exception.ResourceNotFoundException;
import com.realestate.exception.UnauthorizedException;
import com.realestate.model.entity.*;
import com.realestate.repository.*;
import com.realestate.service.InvestmentTransactionService;
import com.realestate.testutil.builder.PortfolioBuilder;
import com.realestate.testutil.builder.PropertyBuilder;
import com.realestate.testutil.builder.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentTransactionServiceTest {

    @Mock
    private InvestmentTransactionRepository transactionRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private HoldingRepository holdingRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private InvestmentTransactionService transactionService;

    private User testUser;
    private Portfolio testPortfolio;
    private Property testProperty;

    @BeforeEach
    void setUp() {
        testUser = UserBuilder.aUser().withId(1L).asInvestor().build();
        testPortfolio = PortfolioBuilder.aPortfolio().withId(1L).withUser(testUser).build();
        testProperty = PropertyBuilder.aProperty().withId(1L).withPrice(100000).withOwner(testUser).build();

        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn(testUser.getUsername());
        lenient().when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
    }

    @Test
    @DisplayName("Should execute buy transaction successfully")
    void executeBuyTransaction_Success() {
        when(transactionRepository.existsByIdempotencyKey(any())).thenReturn(false);
        when(portfolioRepository.findByIdWithLock(1L)).thenReturn(Optional.of(testPortfolio));
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(holdingRepository.findByPortfolioAndProperty(testPortfolio, testProperty)).thenReturn(Optional.empty());
        when(holdingRepository.save(any(Holding.class))).thenAnswer(inv -> {
            Holding h = inv.getArgument(0);
            h.setId(1L);
            return h;
        });
        when(transactionRepository.save(any(InvestmentTransaction.class))).thenAnswer(inv -> {
            InvestmentTransaction t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });
        when(portfolioRepository.save(any(Portfolio.class))).thenAnswer(inv -> inv.getArgument(0));

        InvestmentTransaction result = transactionService.executeBuyTransaction(
                1L, 1L, new BigDecimal("1.0"), "idempotency-key-1");

        assertThat(result).isNotNull();
        assertThat(result.getTransactionType()).isEqualTo(InvestmentTransaction.TransactionType.BUY);
        assertThat(result.getStatus()).isEqualTo(InvestmentTransaction.TransactionStatus.COMPLETED);
        verify(holdingRepository).save(any(Holding.class));
        verify(transactionRepository).save(any(InvestmentTransaction.class));
    }

    @Test
    @DisplayName("Should return existing transaction when idempotency key matches")
    void executeBuyTransaction_IdempotencyCheck() {
        InvestmentTransaction existingTx = InvestmentTransaction.builder()
                .idempotencyKey("existing-key")
                .status(InvestmentTransaction.TransactionStatus.COMPLETED)
                .build();
        existingTx.setId(1L);

        when(transactionRepository.existsByIdempotencyKey("existing-key")).thenReturn(true);
        when(transactionRepository.findByIdempotencyKey("existing-key")).thenReturn(Optional.of(existingTx));

        InvestmentTransaction result = transactionService.executeBuyTransaction(
                1L, 1L, new BigDecimal("1.0"), "existing-key");

        assertThat(result).isEqualTo(existingTx);
        verify(portfolioRepository, never()).findByIdWithLock(any());
    }

    @Test
    @DisplayName("Should throw exception when portfolio not found")
    void executeBuyTransaction_PortfolioNotFound() {
        when(transactionRepository.existsByIdempotencyKey(any())).thenReturn(false);
        when(portfolioRepository.findByIdWithLock(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.executeBuyTransaction(
                999L, 1L, new BigDecimal("1.0"), null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Portfolio not found");
    }

    @Test
    @DisplayName("Should throw exception when not authorized for portfolio")
    void executeBuyTransaction_Unauthorized() {
        User otherUser = UserBuilder.aUser().withId(2L).build();
        Portfolio otherPortfolio = PortfolioBuilder.aPortfolio().withId(2L).withUser(otherUser).build();

        when(transactionRepository.existsByIdempotencyKey(any())).thenReturn(false);
        when(portfolioRepository.findByIdWithLock(2L)).thenReturn(Optional.of(otherPortfolio));

        assertThatThrownBy(() -> transactionService.executeBuyTransaction(
                2L, 1L, new BigDecimal("1.0"), null))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Not authorized to transact on this portfolio");
    }

    @Test
    @DisplayName("Should execute sell transaction successfully")
    void executeSellTransaction_Success() {
        Holding holding = Holding.builder()
                .portfolio(testPortfolio)
                .property(testProperty)
                .quantity(new BigDecimal("2.0"))
                .averageCostBasis(new BigDecimal("100000"))
                .totalCostBasis(new BigDecimal("200000"))
                .status(Holding.HoldingStatus.ACTIVE)
                .build();
        holding.setId(1L);

        when(transactionRepository.existsByIdempotencyKey(any())).thenReturn(false);
        when(portfolioRepository.findByIdWithLock(1L)).thenReturn(Optional.of(testPortfolio));
        when(holdingRepository.findByIdWithLock(1L)).thenReturn(Optional.of(holding));
        when(holdingRepository.save(any(Holding.class))).thenAnswer(inv -> inv.getArgument(0));
        when(transactionRepository.save(any(InvestmentTransaction.class))).thenAnswer(inv -> {
            InvestmentTransaction t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });
        when(portfolioRepository.save(any(Portfolio.class))).thenAnswer(inv -> inv.getArgument(0));

        InvestmentTransaction result = transactionService.executeSellTransaction(
                1L, 1L, new BigDecimal("1.0"), null);

        assertThat(result).isNotNull();
        assertThat(result.getTransactionType()).isEqualTo(InvestmentTransaction.TransactionType.SELL);
        assertThat(result.getStatus()).isEqualTo(InvestmentTransaction.TransactionStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should throw exception when insufficient quantity to sell")
    void executeSellTransaction_InsufficientQuantity() {
        Holding holding = Holding.builder()
                .portfolio(testPortfolio)
                .property(testProperty)
                .quantity(new BigDecimal("0.5"))
                .status(Holding.HoldingStatus.ACTIVE)
                .build();
        holding.setId(1L);

        when(transactionRepository.existsByIdempotencyKey(any())).thenReturn(false);
        when(portfolioRepository.findByIdWithLock(1L)).thenReturn(Optional.of(testPortfolio));
        when(holdingRepository.findByIdWithLock(1L)).thenReturn(Optional.of(holding));

        assertThatThrownBy(() -> transactionService.executeSellTransaction(
                1L, 1L, new BigDecimal("1.0"), null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Insufficient quantity to sell");
    }

    @Test
    @DisplayName("Should throw exception when holding does not belong to portfolio")
    void executeSellTransaction_HoldingNotInPortfolio() {
        Portfolio otherPortfolio = PortfolioBuilder.aPortfolio().withId(2L).withUser(testUser).build();
        Holding holding = Holding.builder()
                .portfolio(otherPortfolio)
                .property(testProperty)
                .quantity(new BigDecimal("1.0"))
                .status(Holding.HoldingStatus.ACTIVE)
                .build();
        holding.setId(1L);

        when(transactionRepository.existsByIdempotencyKey(any())).thenReturn(false);
        when(portfolioRepository.findByIdWithLock(1L)).thenReturn(Optional.of(testPortfolio));
        when(holdingRepository.findByIdWithLock(1L)).thenReturn(Optional.of(holding));

        assertThatThrownBy(() -> transactionService.executeSellTransaction(
                1L, 1L, new BigDecimal("1.0"), null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Holding does not belong to this portfolio");
    }

    @Test
    @DisplayName("Should reverse transaction as admin")
    void reverseTransaction_Success() {
        User adminUser = UserBuilder.aUser().withId(2L).asAdmin().build();
        when(authentication.getName()).thenReturn(adminUser.getUsername());
        when(userRepository.findByUsername(adminUser.getUsername())).thenReturn(Optional.of(adminUser));

        InvestmentTransaction transaction = InvestmentTransaction.builder()
                .status(InvestmentTransaction.TransactionStatus.COMPLETED)
                .build();
        transaction.setId(1L);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(InvestmentTransaction.class))).thenAnswer(inv -> inv.getArgument(0));

        InvestmentTransaction result = transactionService.reverseTransaction(1L);

        assertThat(result.getStatus()).isEqualTo(InvestmentTransaction.TransactionStatus.REVERSED);
    }

    @Test
    @DisplayName("Should throw exception when non-admin tries to reverse")
    void reverseTransaction_NotAdmin_ThrowsException() {
        assertThatThrownBy(() -> transactionService.reverseTransaction(1L))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Only admins can reverse transactions");
    }
}
