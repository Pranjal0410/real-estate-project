package com.realestate.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "portfolios",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"user_id", "portfolio_name"})
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"holdings", "transactions"})
@ToString(exclude = {"holdings", "transactions"})
public class Portfolio extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Portfolio name is required")
    @Size(max = 100)
    @Column(name = "portfolio_name", nullable = false)
    @Builder.Default
    private String portfolioName = "Default Portfolio";

    @Column(name = "total_invested", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal totalInvested = BigDecimal.ZERO;

    @Column(name = "total_current_value", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal totalCurrentValue = BigDecimal.ZERO;

    @Column(name = "unrealized_gains", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal unrealizedGains = BigDecimal.ZERO;

    @Column(name = "realized_gains", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal realizedGains = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_profile", length = 20)
    @Builder.Default
    private RiskProfile riskProfile = RiskProfile.MODERATE;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private PortfolioStatus status = PortfolioStatus.ACTIVE;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Holding> holdings = new HashSet<>();

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<InvestmentTransaction> transactions = new HashSet<>();

    public void recalculateValues() {
        this.totalCurrentValue = holdings.stream()
            .filter(h -> h.getStatus() == Holding.HoldingStatus.ACTIVE)
            .map(Holding::getCurrentValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.unrealizedGains = this.totalCurrentValue.subtract(this.totalInvested);
    }

    public enum RiskProfile {
        CONSERVATIVE,
        MODERATE,
        AGGRESSIVE
    }

    public enum PortfolioStatus {
        ACTIVE,
        SUSPENDED,
        CLOSED
    }
}
