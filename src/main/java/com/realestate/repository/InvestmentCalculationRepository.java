package com.realestate.repository;

import com.realestate.model.entity.InvestmentCalculation;
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

/**
 * JPA repository for InvestmentCalculation entity
 * Provides investment analysis and calculation data access methods
 */
@Repository
public interface InvestmentCalculationRepository extends JpaRepository<InvestmentCalculation, Long> {

    // Derived query methods
    List<InvestmentCalculation> findByUser(User user);
    
    List<InvestmentCalculation> findByProperty(Property property);
    
    List<InvestmentCalculation> findByCalculationType(InvestmentCalculation.CalculationType calculationType);
    
    List<InvestmentCalculation> findByUserAndProperty(User user, Property property);
    
    List<InvestmentCalculation> findByUserId(Long userId);
    
    List<InvestmentCalculation> findByPropertyId(Long propertyId);
    
    List<InvestmentCalculation> findByIsSaved(Boolean isSaved);
    
    List<InvestmentCalculation> findByUserIdAndIsSaved(Long userId, Boolean isSaved);
    
    List<InvestmentCalculation> findByRoiPercentageGreaterThan(BigDecimal roiPercentage);
    
    List<InvestmentCalculation> findByCashFlowGreaterThan(BigDecimal cashFlow);
    
    List<InvestmentCalculation> findByCapRateGreaterThan(BigDecimal capRate);
    
    long countByUser(User user);
    
    long countByProperty(Property property);
    
    long countByCalculationType(InvestmentCalculation.CalculationType calculationType);
    
    boolean existsByUserAndProperty(User user, Property property);

    // Custom JPQL queries
    @Query("SELECT ic FROM InvestmentCalculation ic WHERE ic.calculationDate BETWEEN :startDate AND :endDate")
    List<InvestmentCalculation> findCalculationsByDateRange(@Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT ic FROM InvestmentCalculation ic WHERE ic.user = :user ORDER BY ic.calculationDate DESC")
    Page<InvestmentCalculation> findUserCalculationsOrderByDate(@Param("user") User user, Pageable pageable);

    @Query("SELECT ic FROM InvestmentCalculation ic WHERE ic.property = :property ORDER BY ic.roiPercentage DESC")
    List<InvestmentCalculation> findPropertyCalculationsOrderByROI(@Param("property") Property property);

    @Query("SELECT ic FROM InvestmentCalculation ic WHERE ic.roiPercentage BETWEEN :minRoi AND :maxRoi")
    List<InvestmentCalculation> findCalculationsByROIRange(@Param("minRoi") BigDecimal minRoi,
                                                          @Param("maxRoi") BigDecimal maxRoi);

    @Query("SELECT ic FROM InvestmentCalculation ic WHERE ic.cashFlow > 0 ORDER BY ic.cashFlow DESC")
    List<InvestmentCalculation> findPositiveCashFlowCalculations();

    @Query("SELECT ic FROM InvestmentCalculation ic WHERE ic.breakEvenYears <= :maxYears")
    List<InvestmentCalculation> findQuickBreakEvenCalculations(@Param("maxYears") BigDecimal maxYears);

    @Query("SELECT ic FROM InvestmentCalculation ic WHERE ic.grossRentalYield >= :minYield")
    List<InvestmentCalculation> findHighYieldCalculations(@Param("minYield") BigDecimal minYield);

    @Query("SELECT ic FROM InvestmentCalculation ic LEFT JOIN FETCH ic.property WHERE ic.user = :user")
    List<InvestmentCalculation> findUserCalculationsWithProperty(@Param("user") User user);

    @Query("SELECT ic FROM InvestmentCalculation ic LEFT JOIN FETCH ic.user WHERE ic.property = :property")
    List<InvestmentCalculation> findPropertyCalculationsWithUser(@Param("property") Property property);

    @Query("SELECT ic FROM InvestmentCalculation ic LEFT JOIN FETCH ic.property LEFT JOIN FETCH ic.user WHERE ic.id = :id")
    Optional<InvestmentCalculation> findCalculationWithDetails(@Param("id") Long id);

    // Statistical queries
    @Query("SELECT AVG(ic.roiPercentage) FROM InvestmentCalculation ic WHERE ic.calculationType = :type")
    BigDecimal getAverageROIByType(@Param("type") InvestmentCalculation.CalculationType type);

    @Query("SELECT AVG(ic.cashFlow) FROM InvestmentCalculation ic WHERE ic.user = :user")
    BigDecimal getAverageCashFlowByUser(@Param("user") User user);

    @Query("SELECT MAX(ic.roiPercentage) FROM InvestmentCalculation ic WHERE ic.property = :property")
    BigDecimal getMaxROIForProperty(@Param("property") Property property);

    @Query("SELECT MIN(ic.breakEvenYears) FROM InvestmentCalculation ic WHERE ic.user = :user")
    BigDecimal getMinBreakEvenYearsByUser(@Param("user") User user);

    // Complex analysis queries
    @Query("SELECT ic FROM InvestmentCalculation ic WHERE " +
           "ic.roiPercentage >= :minRoi AND " +
           "ic.cashFlow >= :minCashFlow AND " +
           "ic.capRate >= :minCapRate")
    List<InvestmentCalculation> findGoodInvestmentOpportunities(@Param("minRoi") BigDecimal minRoi,
                                                               @Param("minCashFlow") BigDecimal minCashFlow,
                                                               @Param("minCapRate") BigDecimal minCapRate);

    @Query("SELECT ic FROM InvestmentCalculation ic WHERE " +
           "ic.calculationType = :type AND " +
           "ic.calculationDate >= :since " +
           "ORDER BY ic.roiPercentage DESC")
    List<InvestmentCalculation> findRecentCalculationsByType(@Param("type") InvestmentCalculation.CalculationType type,
                                                           @Param("since") LocalDateTime since);

    // Native SQL queries for advanced analytics
    @Query(value = "SELECT " +
                   "calculation_type, " +
                   "COUNT(*) as calculation_count, " +
                   "AVG(roi_percentage) as avg_roi, " +
                   "AVG(cash_flow) as avg_cash_flow, " +
                   "AVG(cap_rate) as avg_cap_rate " +
                   "FROM investment_calculations " +
                   "GROUP BY calculation_type " +
                   "ORDER BY avg_roi DESC",
           nativeQuery = true)
    List<Object[]> getCalculationStatisticsByType();

    @Query(value = "SELECT ic.*, p.title as property_title, p.location as property_location " +
                   "FROM investment_calculations ic " +
                   "JOIN properties p ON ic.property_id = p.id " +
                   "WHERE ic.roi_percentage > :minRoi " +
                   "AND ic.cash_flow > 0 " +
                   "ORDER BY ic.roi_percentage DESC " +
                   "LIMIT :limit",
           nativeQuery = true)
    List<Object[]> findTopPerformingInvestments(@Param("minRoi") double minRoi, @Param("limit") int limit);

    @Query(value = "SELECT " +
                   "u.username, " +
                   "COUNT(ic.id) as calculation_count, " +
                   "AVG(ic.roi_percentage) as avg_roi, " +
                   "MAX(ic.roi_percentage) as max_roi " +
                   "FROM investment_calculations ic " +
                   "JOIN users u ON ic.user_id = u.id " +
                   "GROUP BY u.id, u.username " +
                   "HAVING COUNT(ic.id) > :minCalculations " +
                   "ORDER BY avg_roi DESC",
           nativeQuery = true)
    List<Object[]> getUserInvestmentPerformance(@Param("minCalculations") int minCalculations);

    @Query(value = "SELECT " +
                   "DATE_FORMAT(calculation_date, '%Y-%m') as month, " +
                   "COUNT(*) as calculation_count, " +
                   "AVG(roi_percentage) as avg_roi " +
                   "FROM investment_calculations " +
                   "WHERE calculation_date >= DATE_SUB(NOW(), INTERVAL 12 MONTH) " +
                   "GROUP BY DATE_FORMAT(calculation_date, '%Y-%m') " +
                   "ORDER BY month",
           nativeQuery = true)
    List<Object[]> getMonthlyCalculationTrends();

    // Portfolio analysis
    @Query("SELECT ic FROM InvestmentCalculation ic " +
           "JOIN ic.property p " +
           "WHERE ic.user = :user " +
           "AND p.propertyType = :propertyType " +
           "ORDER BY ic.roiPercentage DESC")
    List<InvestmentCalculation> findUserCalculationsByPropertyType(@Param("user") User user,
                                                                  @Param("propertyType") Property.PropertyType propertyType);

    @Query(value = "SELECT " +
                   "property_type, " +
                   "COUNT(*) as investment_count, " +
                   "AVG(roi_percentage) as avg_roi, " +
                   "SUM(initial_investment) as total_investment " +
                   "FROM investment_calculations ic " +
                   "JOIN properties p ON ic.property_id = p.id " +
                   "WHERE ic.user_id = :userId " +
                   "GROUP BY p.property_type " +
                   "ORDER BY avg_roi DESC",
           nativeQuery = true)
    List<Object[]> getUserPortfolioAnalysis(@Param("userId") Long userId);

    // Update operations
    @Modifying
    @Query("UPDATE InvestmentCalculation ic SET ic.isSaved = :saved WHERE ic.id = :calculationId")
    int updateSavedStatus(@Param("calculationId") Long calculationId, @Param("saved") Boolean saved);

    @Modifying
    @Query("UPDATE InvestmentCalculation ic SET ic.notes = :notes WHERE ic.id = :calculationId")
    int updateCalculationNotes(@Param("calculationId") Long calculationId, @Param("notes") String notes);

    // Bulk operations
    @Modifying
    @Query("UPDATE InvestmentCalculation ic SET ic.isSaved = false WHERE ic.user = :user AND ic.calculationType = :type")
    int bulkUnsaveCalculationsByUserAndType(@Param("user") User user, 
                                           @Param("type") InvestmentCalculation.CalculationType type);

    @Modifying
    @Query("DELETE FROM InvestmentCalculation ic WHERE ic.user = :user AND ic.isSaved = false AND ic.calculationDate < :cutoffDate")
    int deleteOldUnsavedCalculations(@Param("user") User user, @Param("cutoffDate") LocalDateTime cutoffDate);

    // Soft delete
    @Modifying
    @Query("UPDATE InvestmentCalculation ic SET ic.isDeleted = true WHERE ic.id = :calculationId")
    int softDeleteCalculation(@Param("calculationId") Long calculationId);

    @Query("SELECT ic FROM InvestmentCalculation ic WHERE ic.isDeleted = false")
    List<InvestmentCalculation> findAllActiveCalculations();

    // Comparison queries
    @Query("SELECT ic FROM InvestmentCalculation ic " +
           "WHERE ic.property = :property " +
           "AND ic.calculationType = :type " +
           "ORDER BY ic.calculationDate DESC")
    Page<InvestmentCalculation> findPropertyCalculationsByType(@Param("property") Property property,
                                                              @Param("type") InvestmentCalculation.CalculationType type,
                                                              Pageable pageable);

    // Performance benchmarking
    @Query(value = "SELECT " +
                   "CASE " +
                   "WHEN roi_percentage < 5 THEN 'Low (< 5%)' " +
                   "WHEN roi_percentage < 10 THEN 'Medium (5-10%)' " +
                   "WHEN roi_percentage < 15 THEN 'Good (10-15%)' " +
                   "ELSE 'Excellent (> 15%)' " +
                   "END as roi_category, " +
                   "COUNT(*) as count " +
                   "FROM investment_calculations " +
                   "WHERE roi_percentage IS NOT NULL " +
                   "GROUP BY roi_category " +
                   "ORDER BY count DESC",
           nativeQuery = true)
    List<Object[]> getROIDistribution();
}