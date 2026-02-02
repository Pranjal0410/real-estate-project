package com.realestate.unit.service;

import com.realestate.exception.DuplicateResourceException;
import com.realestate.exception.ResourceNotFoundException;
import com.realestate.exception.UnauthorizedException;
import com.realestate.model.entity.Holding;
import com.realestate.model.entity.Portfolio;
import com.realestate.model.entity.User;
import com.realestate.repository.HoldingRepository;
import com.realestate.repository.PortfolioRepository;
import com.realestate.repository.UserRepository;
import com.realestate.service.PortfolioService;
import com.realestate.testutil.builder.PortfolioBuilder;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private HoldingRepository holdingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PortfolioService portfolioService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = UserBuilder.aUser().withId(1L).asInvestor().build();

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(testUser.getUsername());
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
    }

    @Test
    @DisplayName("Should create portfolio successfully")
    void createPortfolio_Success() {
        when(portfolioRepository.existsByUserAndPortfolioName(testUser, "My Portfolio")).thenReturn(false);
        when(portfolioRepository.save(any(Portfolio.class)))
                .thenAnswer(invocation -> {
                    Portfolio p = invocation.getArgument(0);
                    p.setId(1L);
                    return p;
                });

        Portfolio result = portfolioService.createPortfolio("My Portfolio", Portfolio.RiskProfile.MODERATE);

        assertThat(result).isNotNull();
        assertThat(result.getPortfolioName()).isEqualTo("My Portfolio");
        assertThat(result.getRiskProfile()).isEqualTo(Portfolio.RiskProfile.MODERATE);
        assertThat(result.getStatus()).isEqualTo(Portfolio.PortfolioStatus.ACTIVE);
        verify(portfolioRepository).save(any(Portfolio.class));
    }

    @Test
    @DisplayName("Should throw exception when portfolio name already exists")
    void createPortfolio_DuplicateName_ThrowsException() {
        when(portfolioRepository.existsByUserAndPortfolioName(testUser, "Existing Portfolio")).thenReturn(true);

        assertThatThrownBy(() -> portfolioService.createPortfolio("Existing Portfolio", null))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should get portfolio by ID for owner")
    void getPortfolio_Success() {
        Portfolio portfolio = PortfolioBuilder.aPortfolio().withId(1L).withUser(testUser).build();
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));

        Portfolio result = portfolioService.getPortfolio(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw exception when portfolio not found")
    void getPortfolio_NotFound_ThrowsException() {
        when(portfolioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> portfolioService.getPortfolio(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw exception when accessing another user's portfolio")
    void getPortfolio_Unauthorized_ThrowsException() {
        User otherUser = UserBuilder.aUser().withId(2L).build();
        Portfolio portfolio = PortfolioBuilder.aPortfolio().withId(1L).withUser(otherUser).build();
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));

        assertThatThrownBy(() -> portfolioService.getPortfolio(1L))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Access denied to this portfolio");
    }

    @Test
    @DisplayName("Should allow admin to access any portfolio")
    void getPortfolio_AdminAccess_Success() {
        User adminUser = UserBuilder.aUser().withId(2L).asAdmin().build();
        User otherUser = UserBuilder.aUser().withId(3L).build();
        Portfolio portfolio = PortfolioBuilder.aPortfolio().withId(1L).withUser(otherUser).build();

        when(authentication.getName()).thenReturn(adminUser.getUsername());
        when(userRepository.findByUsername(adminUser.getUsername())).thenReturn(Optional.of(adminUser));
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));

        Portfolio result = portfolioService.getPortfolio(1L);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should get user's portfolios")
    void getMyPortfolios_Success() {
        Portfolio portfolio1 = PortfolioBuilder.aPortfolio().withId(1L).withUser(testUser).build();
        Portfolio portfolio2 = PortfolioBuilder.aPortfolio().withId(2L).withUser(testUser).build();

        when(portfolioRepository.findByUserAndStatus(testUser, Portfolio.PortfolioStatus.ACTIVE))
                .thenReturn(List.of(portfolio1, portfolio2));

        List<Portfolio> result = portfolioService.getMyPortfolios();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should get portfolio holdings")
    void getPortfolioHoldings_Success() {
        Portfolio portfolio = PortfolioBuilder.aPortfolio().withId(1L).withUser(testUser).build();
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStatus(portfolio, Holding.HoldingStatus.ACTIVE))
                .thenReturn(Collections.emptyList());

        List<Holding> result = portfolioService.getPortfolioHoldings(1L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should update portfolio successfully")
    void updatePortfolio_Success() {
        Portfolio portfolio = PortfolioBuilder.aPortfolio().withId(1L).withUser(testUser).build();
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
        when(portfolioRepository.existsByUserAndPortfolioName(testUser, "New Name")).thenReturn(false);
        when(portfolioRepository.save(any(Portfolio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Portfolio result = portfolioService.updatePortfolio(1L, "New Name", Portfolio.RiskProfile.AGGRESSIVE);

        assertThat(result.getPortfolioName()).isEqualTo("New Name");
        assertThat(result.getRiskProfile()).isEqualTo(Portfolio.RiskProfile.AGGRESSIVE);
    }

    @Test
    @DisplayName("Should throw exception when closing portfolio with active holdings")
    void closePortfolio_WithHoldings_ThrowsException() {
        Portfolio portfolio = PortfolioBuilder.aPortfolio().withId(1L).withUser(testUser).build();
        Holding activeHolding = Holding.builder()
                .portfolio(portfolio)
                .quantity(BigDecimal.ONE)
                .status(Holding.HoldingStatus.ACTIVE)
                .build();

        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStatus(portfolio, Holding.HoldingStatus.ACTIVE))
                .thenReturn(List.of(activeHolding));

        assertThatThrownBy(() -> portfolioService.closePortfolio(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot close portfolio with active holdings");
    }

    @Test
    @DisplayName("Should close portfolio successfully when no active holdings")
    void closePortfolio_Success() {
        Portfolio portfolio = PortfolioBuilder.aPortfolio().withId(1L).withUser(testUser).build();

        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStatus(portfolio, Holding.HoldingStatus.ACTIVE))
                .thenReturn(Collections.emptyList());
        when(portfolioRepository.save(any(Portfolio.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        portfolioService.closePortfolio(1L);

        assertThat(portfolio.getStatus()).isEqualTo(Portfolio.PortfolioStatus.CLOSED);
    }

    @Test
    @DisplayName("Should get portfolio summary")
    void getPortfolioSummary_Success() {
        Portfolio portfolio = PortfolioBuilder.aPortfolio()
                .withId(1L)
                .withUser(testUser)
                .withName("Test Portfolio")
                .build();
        portfolio.setTotalInvested(new BigDecimal("100000"));
        portfolio.setTotalCurrentValue(new BigDecimal("110000"));
        portfolio.setUnrealizedGains(new BigDecimal("10000"));

        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));
        when(holdingRepository.findByPortfolioAndStatus(portfolio, Holding.HoldingStatus.ACTIVE))
                .thenReturn(Collections.emptyList());

        PortfolioService.PortfolioSummary summary = portfolioService.getPortfolioSummary(1L);

        assertThat(summary.id()).isEqualTo(1L);
        assertThat(summary.name()).isEqualTo("Test Portfolio");
        assertThat(summary.totalInvested()).isEqualTo(new BigDecimal("100000"));
        assertThat(summary.holdingsCount()).isZero();
    }
}
