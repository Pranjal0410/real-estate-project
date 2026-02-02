package com.realestate.service;

import com.realestate.exception.BadRequestException;
import com.realestate.exception.ResourceNotFoundException;
import com.realestate.exception.UnauthorizedException;
import com.realestate.model.entity.*;
import com.realestate.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InvestmentTransactionService {

    private static final BigDecimal PLATFORM_FEE_RATE = new BigDecimal("0.01"); // 1%

    private final InvestmentTransactionRepository transactionRepository;
    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 100))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public InvestmentTransaction executeBuyTransaction(Long portfolioId, Long propertyId,
                                                        BigDecimal quantity, String idempotencyKey) {
        if (idempotencyKey != null && transactionRepository.existsByIdempotencyKey(idempotencyKey)) {
            return transactionRepository.findByIdempotencyKey(idempotencyKey)
                    .orElseThrow(() -> new BadRequestException("Transaction already processed"));
        }

        User user = getCurrentUser();
        Portfolio portfolio = portfolioRepository.findByIdWithLock(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        if (!portfolio.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Not authorized to transact on this portfolio");
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        BigDecimal unitPrice = property.getPrice();
        BigDecimal grossAmount = unitPrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
        BigDecimal platformFee = grossAmount.multiply(PLATFORM_FEE_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal netAmount = grossAmount.add(platformFee);

        Holding holding = holdingRepository.findByPortfolioAndProperty(portfolio, property)
                .orElse(null);

        if (holding == null) {
            holding = Holding.builder()
                    .portfolio(portfolio)
                    .property(property)
                    .quantity(quantity)
                    .averageCostBasis(unitPrice)
                    .totalCostBasis(grossAmount)
                    .currentValue(grossAmount)
                    .status(Holding.HoldingStatus.ACTIVE)
                    .build();
        } else {
            holding.addQuantity(quantity, unitPrice);
        }

        holding = holdingRepository.save(holding);

        InvestmentTransaction transaction = InvestmentTransaction.builder()
                .portfolio(portfolio)
                .holding(holding)
                .property(property)
                .user(user)
                .transactionType(InvestmentTransaction.TransactionType.BUY)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .grossAmount(grossAmount)
                .platformFee(platformFee)
                .netAmount(netAmount)
                .status(InvestmentTransaction.TransactionStatus.COMPLETED)
                .idempotencyKey(idempotencyKey)
                .build();

        transaction = transactionRepository.save(transaction);

        portfolio.setTotalInvested(portfolio.getTotalInvested().add(grossAmount));
        portfolio.setTotalCurrentValue(portfolio.getTotalCurrentValue().add(grossAmount));
        portfolioRepository.save(portfolio);

        log.info("Executed BUY transaction {} for {} units of property {} at {}",
                transaction.getTransactionReference(), quantity, propertyId, unitPrice);

        return transaction;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public InvestmentTransaction executeSellTransaction(Long portfolioId, Long holdingId,
                                                         BigDecimal quantity, String idempotencyKey) {
        if (idempotencyKey != null && transactionRepository.existsByIdempotencyKey(idempotencyKey)) {
            return transactionRepository.findByIdempotencyKey(idempotencyKey)
                    .orElseThrow(() -> new BadRequestException("Transaction already processed"));
        }

        User user = getCurrentUser();
        Portfolio portfolio = portfolioRepository.findByIdWithLock(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        if (!portfolio.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Not authorized to transact on this portfolio");
        }

        Holding holding = holdingRepository.findByIdWithLock(holdingId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding not found"));

        if (!holding.getPortfolio().getId().equals(portfolioId)) {
            throw new BadRequestException("Holding does not belong to this portfolio");
        }

        if (holding.getQuantity().compareTo(quantity) < 0) {
            throw new BadRequestException("Insufficient quantity to sell");
        }

        Property property = holding.getProperty();
        BigDecimal unitPrice = property.getPrice();
        BigDecimal grossAmount = unitPrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
        BigDecimal platformFee = grossAmount.multiply(PLATFORM_FEE_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal netAmount = grossAmount.subtract(platformFee);

        BigDecimal costBasis = holding.getAverageCostBasis().multiply(quantity).setScale(2, RoundingMode.HALF_UP);
        BigDecimal realizedGainLoss = grossAmount.subtract(costBasis);

        holding.setQuantity(holding.getQuantity().subtract(quantity));
        holding.setTotalCostBasis(holding.getTotalCostBasis().subtract(costBasis));

        if (holding.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            holding.setStatus(Holding.HoldingStatus.SOLD);
        }

        holding.updateCurrentValue(unitPrice);
        holdingRepository.save(holding);

        InvestmentTransaction transaction = InvestmentTransaction.builder()
                .portfolio(portfolio)
                .holding(holding)
                .property(property)
                .user(user)
                .transactionType(InvestmentTransaction.TransactionType.SELL)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .grossAmount(grossAmount)
                .platformFee(platformFee)
                .netAmount(netAmount)
                .costBasis(costBasis)
                .realizedGainLoss(realizedGainLoss)
                .status(InvestmentTransaction.TransactionStatus.COMPLETED)
                .idempotencyKey(idempotencyKey)
                .build();

        transaction = transactionRepository.save(transaction);

        portfolio.setTotalInvested(portfolio.getTotalInvested().subtract(costBasis));
        portfolio.setTotalCurrentValue(portfolio.getTotalCurrentValue().subtract(grossAmount));
        portfolio.setRealizedGains(portfolio.getRealizedGains().add(realizedGainLoss));
        portfolioRepository.save(portfolio);

        log.info("Executed SELL transaction {} for {} units of property {} at {}, realized gain/loss: {}",
                transaction.getTransactionReference(), quantity, property.getId(), unitPrice, realizedGainLoss);

        return transaction;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public InvestmentTransaction executeTransfer(Long fromPortfolioId, Long toPortfolioId,
                                                  Long holdingId, BigDecimal quantity) {
        User user = getCurrentUser();

        Portfolio fromPortfolio = portfolioRepository.findByIdWithLock(fromPortfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Source portfolio not found"));
        Portfolio toPortfolio = portfolioRepository.findByIdWithLock(toPortfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Target portfolio not found"));

        if (!fromPortfolio.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Not authorized to transfer from this portfolio");
        }

        Holding sourceHolding = holdingRepository.findByIdWithLock(holdingId)
                .orElseThrow(() -> new ResourceNotFoundException("Holding not found"));

        if (sourceHolding.getQuantity().compareTo(quantity) < 0) {
            throw new BadRequestException("Insufficient quantity to transfer");
        }

        Property property = sourceHolding.getProperty();
        BigDecimal costBasis = sourceHolding.getAverageCostBasis().multiply(quantity).setScale(2, RoundingMode.HALF_UP);

        sourceHolding.setQuantity(sourceHolding.getQuantity().subtract(quantity));
        sourceHolding.setTotalCostBasis(sourceHolding.getTotalCostBasis().subtract(costBasis));
        if (sourceHolding.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            sourceHolding.setStatus(Holding.HoldingStatus.TRANSFERRED);
        }
        holdingRepository.save(sourceHolding);

        Holding targetHolding = holdingRepository.findByPortfolioAndProperty(toPortfolio, property)
                .orElse(null);

        if (targetHolding == null) {
            targetHolding = Holding.builder()
                    .portfolio(toPortfolio)
                    .property(property)
                    .quantity(quantity)
                    .averageCostBasis(sourceHolding.getAverageCostBasis())
                    .totalCostBasis(costBasis)
                    .status(Holding.HoldingStatus.ACTIVE)
                    .build();
        } else {
            targetHolding.addQuantity(quantity, sourceHolding.getAverageCostBasis());
        }
        holdingRepository.save(targetHolding);

        InvestmentTransaction transaction = InvestmentTransaction.builder()
                .portfolio(fromPortfolio)
                .holding(sourceHolding)
                .property(property)
                .user(user)
                .transactionType(InvestmentTransaction.TransactionType.TRANSFER_OUT)
                .quantity(quantity)
                .unitPrice(sourceHolding.getAverageCostBasis())
                .grossAmount(costBasis)
                .netAmount(costBasis)
                .costBasis(costBasis)
                .status(InvestmentTransaction.TransactionStatus.COMPLETED)
                .build();

        transactionRepository.save(transaction);

        fromPortfolio.setTotalInvested(fromPortfolio.getTotalInvested().subtract(costBasis));
        toPortfolio.setTotalInvested(toPortfolio.getTotalInvested().add(costBasis));
        portfolioRepository.save(fromPortfolio);
        portfolioRepository.save(toPortfolio);

        log.info("Transferred {} units from portfolio {} to portfolio {}", quantity, fromPortfolioId, toPortfolioId);

        return transaction;
    }

    @Transactional(readOnly = true)
    public List<InvestmentTransaction> getTransactionHistory(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        User user = getCurrentUser();
        if (!portfolio.getUser().getId().equals(user.getId()) &&
            user.getRole() != User.UserRole.ADMIN &&
            user.getRole() != User.UserRole.ANALYST) {
            throw new UnauthorizedException("Access denied");
        }

        return transactionRepository.findByPortfolio(portfolio);
    }

    @Transactional(readOnly = true)
    public InvestmentTransaction getTransaction(String transactionReference) {
        return transactionRepository.findByTransactionReference(transactionReference)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public InvestmentTransaction reverseTransaction(Long transactionId) {
        User user = getCurrentUser();
        if (user.getRole() != User.UserRole.ADMIN) {
            throw new UnauthorizedException("Only admins can reverse transactions");
        }

        InvestmentTransaction original = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (original.getStatus() != InvestmentTransaction.TransactionStatus.COMPLETED) {
            throw new BadRequestException("Only completed transactions can be reversed");
        }

        original.setStatus(InvestmentTransaction.TransactionStatus.REVERSED);
        transactionRepository.save(original);

        log.info("Reversed transaction {}", original.getTransactionReference());
        return original;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
}
