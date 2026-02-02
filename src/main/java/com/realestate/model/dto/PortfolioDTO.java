package com.realestate.model.dto;

import com.realestate.model.entity.Portfolio;
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
public class PortfolioDTO {

    private Long id;
    private Long userId;
    private String username;
    private String portfolioName;
    private BigDecimal totalInvested;
    private BigDecimal totalCurrentValue;
    private BigDecimal unrealizedGains;
    private BigDecimal realizedGains;
    private Portfolio.RiskProfile riskProfile;
    private Portfolio.PortfolioStatus status;
    private Integer holdingsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PortfolioDTO fromEntity(Portfolio portfolio) {
        return PortfolioDTO.builder()
                .id(portfolio.getId())
                .userId(portfolio.getUser().getId())
                .username(portfolio.getUser().getUsername())
                .portfolioName(portfolio.getPortfolioName())
                .totalInvested(portfolio.getTotalInvested())
                .totalCurrentValue(portfolio.getTotalCurrentValue())
                .unrealizedGains(portfolio.getUnrealizedGains())
                .realizedGains(portfolio.getRealizedGains())
                .riskProfile(portfolio.getRiskProfile())
                .status(portfolio.getStatus())
                .holdingsCount(portfolio.getHoldings() != null ? portfolio.getHoldings().size() : 0)
                .createdAt(portfolio.getCreatedAt())
                .updatedAt(portfolio.getUpdatedAt())
                .build();
    }
}
