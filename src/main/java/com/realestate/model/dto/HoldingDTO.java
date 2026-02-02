package com.realestate.model.dto;

import com.realestate.model.entity.Holding;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoldingDTO {

    private Long id;
    private Long portfolioId;
    private Long propertyId;
    private String propertyTitle;
    private String propertyLocation;
    private BigDecimal quantity;
    private BigDecimal averageCostBasis;
    private BigDecimal totalCostBasis;
    private BigDecimal currentValue;
    private BigDecimal unrealizedGainLoss;
    private BigDecimal unrealizedGainLossPercentage;
    private Holding.HoldingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static HoldingDTO fromEntity(Holding holding) {
        return HoldingDTO.builder()
                .id(holding.getId())
                .portfolioId(holding.getPortfolio().getId())
                .propertyId(holding.getProperty().getId())
                .propertyTitle(holding.getProperty().getTitle())
                .propertyLocation(holding.getProperty().getLocation())
                .quantity(holding.getQuantity())
                .averageCostBasis(holding.getAverageCostBasis())
                .totalCostBasis(holding.getTotalCostBasis())
                .currentValue(holding.getCurrentValue())
                .unrealizedGainLoss(holding.getUnrealizedGainLoss())
                .unrealizedGainLossPercentage(holding.getUnrealizedGainLossPercentage())
                .status(holding.getStatus())
                .createdAt(holding.getCreatedAt())
                .updatedAt(holding.getUpdatedAt())
                .build();
    }
}
