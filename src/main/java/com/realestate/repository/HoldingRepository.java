package com.realestate.repository;

import com.realestate.model.entity.Holding;
import com.realestate.model.entity.Portfolio;
import com.realestate.model.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {

    List<Holding> findByPortfolio(Portfolio portfolio);

    List<Holding> findByPortfolioAndStatus(Portfolio portfolio, Holding.HoldingStatus status);

    Optional<Holding> findByPortfolioAndProperty(Portfolio portfolio, Property property);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM Holding h WHERE h.id = :id")
    Optional<Holding> findByIdWithLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM Holding h WHERE h.portfolio = :portfolio AND h.property = :property AND h.status = 'ACTIVE'")
    Optional<Holding> findActiveHoldingWithLock(@Param("portfolio") Portfolio portfolio, @Param("property") Property property);

    @Query("SELECT h FROM Holding h WHERE h.portfolio.id = :portfolioId AND h.status = 'ACTIVE'")
    List<Holding> findActiveHoldingsByPortfolioId(@Param("portfolioId") Long portfolioId);

    @Query("SELECT h FROM Holding h WHERE h.property = :property AND h.status = 'ACTIVE'")
    List<Holding> findActiveHoldingsByProperty(@Param("property") Property property);

    @Query("SELECT SUM(h.quantity) FROM Holding h WHERE h.property = :property AND h.status = 'ACTIVE'")
    Optional<java.math.BigDecimal> getTotalQuantityByProperty(@Param("property") Property property);
}
