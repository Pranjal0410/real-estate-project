package com.realestate.testutil;

import com.realestate.model.entity.*;
import com.realestate.testutil.builder.PortfolioBuilder;
import com.realestate.testutil.builder.PropertyBuilder;
import com.realestate.testutil.builder.UserBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataFactory {

    public static User createInvestor() {
        return UserBuilder.aUser()
                .asInvestor()
                .withUsername("investor_" + UUID.randomUUID().toString().substring(0, 8))
                .withEmail("investor_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com")
                .build();
    }

    public static User createAdmin() {
        return UserBuilder.aUser()
                .asAdmin()
                .withUsername("admin_" + UUID.randomUUID().toString().substring(0, 8))
                .withEmail("admin_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com")
                .build();
    }

    public static User createAnalyst() {
        return UserBuilder.aUser()
                .asAnalyst()
                .withUsername("analyst_" + UUID.randomUUID().toString().substring(0, 8))
                .withEmail("analyst_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com")
                .build();
    }

    public static Property createProperty(User owner) {
        return PropertyBuilder.aProperty()
                .withOwner(owner)
                .withTitle("Property " + UUID.randomUUID().toString().substring(0, 8))
                .withPrice(new BigDecimal("500000.00"))
                .build();
    }

    public static Portfolio createPortfolio(User user) {
        return PortfolioBuilder.aPortfolio()
                .withUser(user)
                .withName("Portfolio " + UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    public static Holding createHolding(Portfolio portfolio, Property property) {
        return Holding.builder()
                .portfolio(portfolio)
                .property(property)
                .quantity(new BigDecimal("1.0"))
                .averageCostBasis(property.getPrice())
                .totalCostBasis(property.getPrice())
                .currentValue(property.getPrice())
                .status(Holding.HoldingStatus.ACTIVE)
                .build();
    }

    public static InvestmentTransaction createBuyTransaction(Portfolio portfolio, Property property, User user) {
        return InvestmentTransaction.builder()
                .portfolio(portfolio)
                .property(property)
                .user(user)
                .transactionType(InvestmentTransaction.TransactionType.BUY)
                .quantity(new BigDecimal("1.0"))
                .unitPrice(property.getPrice())
                .grossAmount(property.getPrice())
                .netAmount(property.getPrice())
                .status(InvestmentTransaction.TransactionStatus.COMPLETED)
                .build();
    }

    public static RefreshToken createRefreshToken(User user) {
        return RefreshToken.builder()
                .tokenHash(UUID.randomUUID().toString())
                .familyId(UUID.randomUUID().toString())
                .user(user)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .status(RefreshToken.TokenStatus.ACTIVE)
                .ipAddress("127.0.0.1")
                .userAgent("Test Agent")
                .build();
    }

    public static TokenBlacklist createBlacklistedToken() {
        return TokenBlacklist.builder()
                .jti(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .blacklistedAt(LocalDateTime.now())
                .reason(TokenBlacklist.BlacklistReason.LOGOUT)
                .build();
    }
}
