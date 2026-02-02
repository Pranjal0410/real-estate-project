package com.realestate.model.dto;

import com.realestate.model.entity.InvestmentTransaction;
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
public class TransactionDTO {

    private Long id;
    private String transactionReference;
    private Long portfolioId;
    private Long holdingId;
    private Long propertyId;
    private String propertyTitle;
    private Long userId;
    private String username;
    private InvestmentTransaction.TransactionType transactionType;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal grossAmount;
    private BigDecimal netAmount;
    private BigDecimal platformFee;
    private BigDecimal transactionFee;
    private BigDecimal taxAmount;
    private BigDecimal costBasis;
    private BigDecimal realizedGainLoss;
    private InvestmentTransaction.TransactionStatus status;
    private LocalDateTime createdAt;

    public static TransactionDTO fromEntity(InvestmentTransaction tx) {
        return TransactionDTO.builder()
                .id(tx.getId())
                .transactionReference(tx.getTransactionReference())
                .portfolioId(tx.getPortfolio().getId())
                .holdingId(tx.getHolding() != null ? tx.getHolding().getId() : null)
                .propertyId(tx.getProperty().getId())
                .propertyTitle(tx.getProperty().getTitle())
                .userId(tx.getUser().getId())
                .username(tx.getUser().getUsername())
                .transactionType(tx.getTransactionType())
                .quantity(tx.getQuantity())
                .unitPrice(tx.getUnitPrice())
                .grossAmount(tx.getGrossAmount())
                .netAmount(tx.getNetAmount())
                .platformFee(tx.getPlatformFee())
                .transactionFee(tx.getTransactionFee())
                .taxAmount(tx.getTaxAmount())
                .costBasis(tx.getCostBasis())
                .realizedGainLoss(tx.getRealizedGainLoss())
                .status(tx.getStatus())
                .createdAt(tx.getCreatedAt())
                .build();
    }
}
