package com.realestate.repository;

import com.realestate.model.entity.Portfolio;
import com.realestate.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByUser(User user);

    List<Portfolio> findByUserAndStatus(User user, Portfolio.PortfolioStatus status);

    Optional<Portfolio> findByUserAndPortfolioName(User user, String portfolioName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Portfolio p WHERE p.id = :id")
    Optional<Portfolio> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT p FROM Portfolio p WHERE p.user.id = :userId AND p.status = 'ACTIVE'")
    List<Portfolio> findActivePortfoliosByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(p.totalCurrentValue) FROM Portfolio p WHERE p.user = :user AND p.status = 'ACTIVE'")
    Optional<java.math.BigDecimal> getTotalValueByUser(@Param("user") User user);

    @Query("SELECT COUNT(p) FROM Portfolio p WHERE p.status = 'ACTIVE'")
    long countActivePortfolios();

    boolean existsByUserAndPortfolioName(User user, String portfolioName);
}
