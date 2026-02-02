package com.realestate.repository;

import com.realestate.model.entity.InvestmentTransaction;
import com.realestate.model.entity.Portfolio;
import com.realestate.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentTransactionRepository extends JpaRepository<InvestmentTransaction, Long> {

    Optional<InvestmentTransaction> findByTransactionReference(String transactionReference);

    Optional<InvestmentTransaction> findByIdempotencyKey(String idempotencyKey);

    List<InvestmentTransaction> findByUser(User user);

    Page<InvestmentTransaction> findByUser(User user, Pageable pageable);

    List<InvestmentTransaction> findByPortfolio(Portfolio portfolio);

    List<InvestmentTransaction> findByUserAndStatus(User user, InvestmentTransaction.TransactionStatus status);

    @Query("SELECT t FROM InvestmentTransaction t WHERE t.user = :user ORDER BY t.createdAt DESC")
    Page<InvestmentTransaction> findRecentByUser(@Param("user") User user, Pageable pageable);

    @Query("SELECT t FROM InvestmentTransaction t WHERE t.status = 'PENDING' ORDER BY t.createdAt ASC")
    List<InvestmentTransaction> findPendingTransactions();

    @Query("SELECT SUM(t.netAmount) FROM InvestmentTransaction t WHERE t.user = :user AND t.transactionType = 'BUY' AND t.status = 'COMPLETED'")
    Optional<BigDecimal> getTotalInvestedByUser(@Param("user") User user);

    @Query("SELECT SUM(t.realizedGainLoss) FROM InvestmentTransaction t WHERE t.user = :user AND t.transactionType = 'SELL' AND t.status = 'COMPLETED'")
    Optional<BigDecimal> getTotalRealizedGainsByUser(@Param("user") User user);

    @Query("SELECT COUNT(t) FROM InvestmentTransaction t WHERE t.createdAt >= :since AND t.status = 'COMPLETED'")
    long countCompletedTransactionsSince(@Param("since") LocalDateTime since);

    @Query("SELECT SUM(t.netAmount) FROM InvestmentTransaction t WHERE t.createdAt >= :since AND t.status = 'COMPLETED'")
    Optional<BigDecimal> getTotalVolumeSince(@Param("since") LocalDateTime since);

    boolean existsByIdempotencyKey(String idempotencyKey);
}
