package com.realestate.service;

import com.realestate.exception.DuplicateResourceException;
import com.realestate.exception.ResourceNotFoundException;
import com.realestate.exception.UnauthorizedException;
import com.realestate.model.entity.Holding;
import com.realestate.model.entity.Portfolio;
import com.realestate.model.entity.User;
import com.realestate.repository.HoldingRepository;
import com.realestate.repository.PortfolioRepository;
import com.realestate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    private final UserRepository userRepository;

    public Portfolio createPortfolio(String portfolioName, Portfolio.RiskProfile riskProfile) {
        User user = getCurrentUser();

        if (portfolioRepository.existsByUserAndPortfolioName(user, portfolioName)) {
            throw new DuplicateResourceException("Portfolio with name '" + portfolioName + "' already exists");
        }

        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .portfolioName(portfolioName)
                .riskProfile(riskProfile != null ? riskProfile : Portfolio.RiskProfile.MODERATE)
                .status(Portfolio.PortfolioStatus.ACTIVE)
                .totalInvested(BigDecimal.ZERO)
                .totalCurrentValue(BigDecimal.ZERO)
                .unrealizedGains(BigDecimal.ZERO)
                .realizedGains(BigDecimal.ZERO)
                .build();

        Portfolio saved = portfolioRepository.save(portfolio);
        log.info("Created portfolio '{}' for user {}", portfolioName, user.getUsername());
        return saved;
    }

    @Transactional(readOnly = true)
    public Portfolio getPortfolio(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id: " + id));

        User currentUser = getCurrentUser();
        if (!portfolio.getUser().getId().equals(currentUser.getId()) &&
            currentUser.getRole() != User.UserRole.ADMIN &&
            currentUser.getRole() != User.UserRole.ANALYST) {
            throw new UnauthorizedException("Access denied to this portfolio");
        }

        return portfolio;
    }

    @Transactional(readOnly = true)
    public List<Portfolio> getMyPortfolios() {
        User user = getCurrentUser();
        return portfolioRepository.findByUserAndStatus(user, Portfolio.PortfolioStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Holding> getPortfolioHoldings(Long portfolioId) {
        Portfolio portfolio = getPortfolio(portfolioId);
        return holdingRepository.findByPortfolioAndStatus(portfolio, Holding.HoldingStatus.ACTIVE);
    }

    public Portfolio updatePortfolio(Long id, String portfolioName, Portfolio.RiskProfile riskProfile) {
        Portfolio portfolio = getPortfolio(id);
        User currentUser = getCurrentUser();

        if (!portfolio.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Cannot update another user's portfolio");
        }

        if (portfolioName != null && !portfolioName.equals(portfolio.getPortfolioName())) {
            if (portfolioRepository.existsByUserAndPortfolioName(currentUser, portfolioName)) {
                throw new DuplicateResourceException("Portfolio with name '" + portfolioName + "' already exists");
            }
            portfolio.setPortfolioName(portfolioName);
        }

        if (riskProfile != null) {
            portfolio.setRiskProfile(riskProfile);
        }

        return portfolioRepository.save(portfolio);
    }

    public void recalculatePortfolioValues(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findByIdWithLock(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found"));

        List<Holding> holdings = holdingRepository.findByPortfolioAndStatus(portfolio, Holding.HoldingStatus.ACTIVE);

        BigDecimal totalCurrentValue = BigDecimal.ZERO;
        BigDecimal totalCostBasis = BigDecimal.ZERO;

        for (Holding holding : holdings) {
            holding.updateCurrentValue(holding.getProperty().getPrice());
            holdingRepository.save(holding);
            totalCurrentValue = totalCurrentValue.add(holding.getCurrentValue());
            totalCostBasis = totalCostBasis.add(holding.getTotalCostBasis());
        }

        portfolio.setTotalCurrentValue(totalCurrentValue);
        portfolio.setTotalInvested(totalCostBasis);
        portfolio.setUnrealizedGains(totalCurrentValue.subtract(totalCostBasis));

        portfolioRepository.save(portfolio);
        log.info("Recalculated portfolio {} values", portfolioId);
    }

    public void closePortfolio(Long id) {
        Portfolio portfolio = getPortfolio(id);
        User currentUser = getCurrentUser();

        if (!portfolio.getUser().getId().equals(currentUser.getId()) &&
            currentUser.getRole() != User.UserRole.ADMIN) {
            throw new UnauthorizedException("Cannot close another user's portfolio");
        }

        List<Holding> activeHoldings = holdingRepository.findByPortfolioAndStatus(portfolio, Holding.HoldingStatus.ACTIVE);
        if (!activeHoldings.isEmpty()) {
            throw new IllegalStateException("Cannot close portfolio with active holdings");
        }

        portfolio.setStatus(Portfolio.PortfolioStatus.CLOSED);
        portfolioRepository.save(portfolio);
        log.info("Closed portfolio {}", id);
    }

    @Transactional(readOnly = true)
    public PortfolioSummary getPortfolioSummary(Long portfolioId) {
        Portfolio portfolio = getPortfolio(portfolioId);
        List<Holding> holdings = holdingRepository.findByPortfolioAndStatus(portfolio, Holding.HoldingStatus.ACTIVE);

        return new PortfolioSummary(
                portfolio.getId(),
                portfolio.getPortfolioName(),
                portfolio.getTotalInvested(),
                portfolio.getTotalCurrentValue(),
                portfolio.getUnrealizedGains(),
                portfolio.getRealizedGains(),
                holdings.size(),
                portfolio.getRiskProfile()
        );
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    public record PortfolioSummary(
            Long id,
            String name,
            BigDecimal totalInvested,
            BigDecimal totalCurrentValue,
            BigDecimal unrealizedGains,
            BigDecimal realizedGains,
            int holdingsCount,
            Portfolio.RiskProfile riskProfile
    ) {}
}
