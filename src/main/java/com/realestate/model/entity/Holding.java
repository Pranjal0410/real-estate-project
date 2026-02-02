package com.realestate.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "holdings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"portfolio", "property"})
@ToString(exclude = {"portfolio", "property"})
public class Holding extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal quantity;

    @Column(name = "average_cost_basis", nullable = false, precision = 18, scale = 2)
    private BigDecimal averageCostBasis;

    @Column(name = "total_cost_basis", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalCostBasis;

    @Column(name = "current_value", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal currentValue = BigDecimal.ZERO;

    @Column(name = "unrealized_gain_loss", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal unrealizedGainLoss = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private HoldingStatus status = HoldingStatus.ACTIVE;

    public void updateCurrentValue(BigDecimal currentPrice) {
        this.currentValue = quantity.multiply(currentPrice).setScale(2, RoundingMode.HALF_UP);
        this.unrealizedGainLoss = this.currentValue.subtract(this.totalCostBasis);
    }

    public BigDecimal getUnrealizedGainLossPercentage() {
        if (totalCostBasis.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return unrealizedGainLoss.divide(totalCostBasis, 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }

    public void addQuantity(BigDecimal qty, BigDecimal price) {
        BigDecimal newCost = qty.multiply(price);
        BigDecimal totalQty = this.quantity.add(qty);
        BigDecimal totalCost = this.totalCostBasis.add(newCost);

        this.quantity = totalQty;
        this.totalCostBasis = totalCost;
        this.averageCostBasis = totalCost.divide(totalQty, 2, RoundingMode.HALF_UP);
    }

    public enum HoldingStatus {
        ACTIVE,
        SOLD,
        TRANSFERRED
    }
}
