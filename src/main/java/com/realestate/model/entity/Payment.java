package com.realestate.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_investor", columnList = "investor_id"),
    @Index(name = "idx_payment_property", columnList = "property_id"),
    @Index(name = "idx_payment_razorpay_order", columnList = "razorpay_order_id"),
    @Index(name = "idx_payment_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"investor", "property"})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id", nullable = false)
    private User investor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column
    private Double amountInRupees;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal investmentAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 20)
    @Builder.Default
    private PaymentType paymentType = PaymentType.INVESTMENT;

    @Column(name = "razorpay_order_id", unique = true, length = 100)
    private String razorpayOrderId;

    @Column(name = "razorpay_payment_id", unique = true, length = 100)
    private String razorpayPaymentId;

    @Column(name = "razorpay_signature", length = 500)
    private String razorpaySignature;

    @Column(name = "order_id", unique = true, length = 100)
    private String orderId;

    @Column(name = "payment_id", unique = true, length = 100)
    private String paymentId;

    @Column(name = "signature", length = 500)
    private String signature;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "currency", length = 10)
    private String currency = "INR";

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "email_sent")
    private boolean emailSent = false;

    @Version
    private Long version;

    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        SUCCESS,
        FAILED,
        REFUNDED,
        PARTIALLY_REFUNDED,
        EXPIRED,
        COMPLETED
    }

    public enum PaymentType {
        INVESTMENT,
        DOWNPAYMENT,
        BOOKING,
        MAINTENANCE
    }

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = PaymentStatus.PENDING;
        }
        if (currency == null) {
            currency = "INR";
        }
    }

    public void markAsSuccess(String paymentId, String signature) {
        this.status = PaymentStatus.SUCCESS;
        this.razorpayPaymentId = paymentId;
        this.razorpaySignature = signature;
        this.paidAt = LocalDateTime.now();
    }

    public void markAsFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    public boolean isPaid() {
        return status == PaymentStatus.SUCCESS;
    }

    public boolean isPending() {
        return status == PaymentStatus.PENDING || status == PaymentStatus.PROCESSING;
    }
}