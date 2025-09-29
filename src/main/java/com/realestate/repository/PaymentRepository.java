package com.realestate.repository;

import com.realestate.model.entity.Payment;
import com.realestate.model.entity.Payment.PaymentStatus;
import com.realestate.model.entity.Property;
import com.realestate.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    Optional<Payment> findByOrderId(String orderId);

    List<Payment> findByInvestorOrderByCreatedAtDesc(User investor);

    Page<Payment> findByInvestor(User investor, Pageable pageable);

    List<Payment> findByPropertyOrderByCreatedAtDesc(Property property);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByInvestorAndStatus(User investor, PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.investor.id = :investorId " +
           "AND p.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY p.createdAt DESC")
    List<Payment> findInvestorPaymentsByDateRange(
        @Param("investorId") Long investorId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT p FROM Payment p WHERE p.property.id = :propertyId " +
           "AND p.status = :status")
    List<Payment> findByPropertyAndStatus(
        @Param("propertyId") Long propertyId,
        @Param("status") PaymentStatus status
    );

    @Query("SELECT SUM(p.investmentAmount) FROM Payment p " +
           "WHERE p.investor.id = :investorId AND p.status = 'SUCCESS'")
    BigDecimal getTotalInvestmentByInvestor(@Param("investorId") Long investorId);

    @Query("SELECT SUM(p.investmentAmount) FROM Payment p " +
           "WHERE p.property.id = :propertyId AND p.status = 'SUCCESS'")
    BigDecimal getTotalInvestmentForProperty(@Param("propertyId") Long propertyId);

    @Query("SELECT COUNT(p) FROM Payment p " +
           "WHERE p.investor.id = :investorId AND p.status = 'SUCCESS'")
    Long getSuccessfulPaymentCountByInvestor(@Param("investorId") Long investorId);

    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' " +
           "AND p.createdAt < :expiryTime")
    List<Payment> findExpiredPayments(@Param("expiryTime") LocalDateTime expiryTime);

    @Modifying
    @Query("UPDATE Payment p SET p.status = 'EXPIRED' " +
           "WHERE p.status = 'PENDING' AND p.createdAt < :expiryTime")
    int expireOldPayments(@Param("expiryTime") LocalDateTime expiryTime);

    @Query("SELECT p FROM Payment p WHERE p.status = 'SUCCESS' " +
           "AND p.emailSent = false")
    List<Payment> findPendingEmailNotifications();

    @Query(value = "SELECT DATE(p.created_at) as date, " +
                   "COUNT(*) as count, " +
                   "SUM(p.investment_amount) as total " +
                   "FROM payments p " +
                   "WHERE p.status = 'SUCCESS' " +
                   "AND p.created_at >= :startDate " +
                   "GROUP BY DATE(p.created_at) " +
                   "ORDER BY date DESC",
           nativeQuery = true)
    List<Object[]> getDailyPaymentStatistics(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT p.property, COUNT(p), SUM(p.investmentAmount) " +
           "FROM Payment p " +
           "WHERE p.status = 'SUCCESS' " +
           "GROUP BY p.property " +
           "ORDER BY SUM(p.investmentAmount) DESC")
    List<Object[]> getTopInvestedProperties(Pageable pageable);

    boolean existsByInvestorAndPropertyAndStatus(User investor, Property property, PaymentStatus status);
}