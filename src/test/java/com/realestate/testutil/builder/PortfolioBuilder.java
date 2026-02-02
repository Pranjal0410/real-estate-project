package com.realestate.testutil.builder;

import com.realestate.model.entity.Portfolio;
import com.realestate.model.entity.User;

import java.math.BigDecimal;

public class PortfolioBuilder {

    private Long id = 1L;
    private User user;
    private String portfolioName = "Test Portfolio";
    private BigDecimal totalInvested = BigDecimal.ZERO;
    private BigDecimal totalCurrentValue = BigDecimal.ZERO;
    private BigDecimal unrealizedGains = BigDecimal.ZERO;
    private BigDecimal realizedGains = BigDecimal.ZERO;
    private Portfolio.RiskProfile riskProfile = Portfolio.RiskProfile.MODERATE;
    private Portfolio.PortfolioStatus status = Portfolio.PortfolioStatus.ACTIVE;

    public static PortfolioBuilder aPortfolio() {
        return new PortfolioBuilder();
    }

    public PortfolioBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PortfolioBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public PortfolioBuilder withName(String name) {
        this.portfolioName = name;
        return this;
    }

    public PortfolioBuilder withTotalInvested(BigDecimal totalInvested) {
        this.totalInvested = totalInvested;
        return this;
    }

    public PortfolioBuilder withRiskProfile(Portfolio.RiskProfile riskProfile) {
        this.riskProfile = riskProfile;
        return this;
    }

    public PortfolioBuilder conservative() {
        this.riskProfile = Portfolio.RiskProfile.CONSERVATIVE;
        return this;
    }

    public PortfolioBuilder aggressive() {
        this.riskProfile = Portfolio.RiskProfile.AGGRESSIVE;
        return this;
    }

    public PortfolioBuilder closed() {
        this.status = Portfolio.PortfolioStatus.CLOSED;
        return this;
    }

    public Portfolio build() {
        if (user == null) {
            user = UserBuilder.aUser().build();
        }

        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .portfolioName(portfolioName)
                .totalInvested(totalInvested)
                .totalCurrentValue(totalCurrentValue)
                .unrealizedGains(unrealizedGains)
                .realizedGains(realizedGains)
                .riskProfile(riskProfile)
                .status(status)
                .build();
        portfolio.setId(id);
        return portfolio;
    }
}
