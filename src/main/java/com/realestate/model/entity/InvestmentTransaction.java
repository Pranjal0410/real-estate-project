package com.realestate.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Table(name = "investment_transactions",
       indexes = {
           @Index(name = "idx_txn_user_status", columnList = "user_id, status"),
           @Index(name = "idx_txn_reference", columnList = "transaction_reference")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"portfolio", "holding", "property", "user"})
@ToString(exclude = {"portfolio", "holding", "property", "user"})
public class InvestmentTransaction extends BaseEntity {

    private static final AtomicLong SEQUENCE = new AtomicLong(0);

    @Column(name = "transaction_reference", nullable = false, unique = true, length = 50)
    private String transactionReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holding_id")
    private Holding holding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "gross_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal grossAmount;

    @Column(name = "net_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "platform_fee", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal platformFee = BigDecimal.ZERO;

    @Column(name = "transaction_fee", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal transactionFee = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "cost_basis", precision = 18, scale = 2)
    private BigDecimal costBasis;

    @Column(name = "realized_gain_loss", precision = 18, scale = 2)
    private BigDecimal realizedGainLoss;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "idempotency_key", unique = true, length = 100)
    private String idempotencyKey;

    @PrePersist
    protected void onCreate() {
        if (transactionReference == null) {
            transactionReference = generateTransactionReference();
        }
    }

    private String generateTransactionReference() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = SEQUENCE.incrementAndGet() % 100000;
        return String.format("TXN-%s-%05d", date, seq);
    }

    public BigDecimal getTotalFees() {
        return platformFee.add(transactionFee).add(taxAmount);
    }

    public enum TransactionType {
        BUY,
        SELL,
        DIVIDEND,
        TRANSFER_IN,
        TRANSFER_OUT
    }

    public enum TransactionStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        CANCELLED,
        REVERSED
    }
}
