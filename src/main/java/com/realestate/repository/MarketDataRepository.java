package com.realestate.repository;

import com.realestate.model.entity.MarketData;
import com.realestate.model.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository for MarketData entity
 * Provides market analysis and trends data access methods
 */
@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {

    // Derived query methods
    List<MarketData> findByLocation(String location);
    
    List<MarketData> findByCity(String city);
    
    List<MarketData> findByState(String state);
    
    List<MarketData> findByPincode(String pincode);
    
    List<MarketData> findByCityAndState(String city, String state);
    
    List<MarketData> findByPropertyType(Property.PropertyType propertyType);
    
    List<MarketData> findByMarketTrend(MarketData.MarketTrend marketTrend);
    
    List<MarketData> findByDataSource(String dataSource);
    
    List<MarketData> findByDataDate(LocalDate dataDate);
    
    List<MarketData> findByDataDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<MarketData> findByAvgPricePerSqftGreaterThan(BigDecimal avgPricePerSqft);
    
    List<MarketData> findByGrowthRateYoyGreaterThan(BigDecimal growthRateYoy);
    
    List<MarketData> findByVacancyRateLessThan(BigDecimal vacancyRate);
    
    List<MarketData> findByLocationContainingIgnoreCase(String location);
    
    long countByPropertyType(Property.PropertyType propertyType);
    
    long countByMarketTrend(MarketData.MarketTrend marketTrend);
    
    boolean existsByLocationAndDataDate(String location, LocalDate dataDate);

    // Custom JPQL queries
    @Query("SELECT md FROM MarketData md WHERE md.location = :location ORDER BY md.dataDate DESC")
    List<MarketData> findLatestMarketDataByLocation(@Param("location") String location);

    @Query("SELECT md FROM MarketData md WHERE md.city = :city AND md.state = :state ORDER BY md.dataDate DESC")
    Page<MarketData> findLatestMarketDataByCityState(@Param("city") String city, 
                                                    @Param("state") String state, 
                                                    Pageable pageable);

    @Query("SELECT md FROM MarketData md WHERE md.propertyType = :propertyType AND md.dataDate = :date")
    List<MarketData> findMarketDataByTypeAndDate(@Param("propertyType") Property.PropertyType propertyType,
                                                @Param("date") LocalDate date);

    @Query("SELECT md FROM MarketData md WHERE md.dataDate >= :startDate ORDER BY md.dataDate DESC")
    List<MarketData> findRecentMarketData(@Param("startDate") LocalDate startDate);

    @Query("SELECT md FROM MarketData md WHERE " +
           "md.growthRateYoy > :minGrowth AND " +
           "md.vacancyRate < :maxVacancy AND " +
           "md.marketTrend = :trend")
    List<MarketData> findHotMarkets(@Param("minGrowth") BigDecimal minGrowth,
                                   @Param("maxVacancy") BigDecimal maxVacancy,
                                   @Param("trend") MarketData.MarketTrend trend);

    @Query("SELECT md FROM MarketData md WHERE " +
           "md.location = :location AND " +
           "md.propertyType = :propertyType " +
           "ORDER BY md.dataDate DESC")
    List<MarketData> findLocationTypeHistory(@Param("location") String location,
                                           @Param("propertyType") Property.PropertyType propertyType);

    @Query("SELECT md FROM MarketData md WHERE " +
           "md.avgDaysOnMarket <= :maxDays AND " +
           "md.absorptionRate >= :minAbsorption")
    List<MarketData> findFastMovingMarkets(@Param("maxDays") Integer maxDays,
                                         @Param("minAbsorption") BigDecimal minAbsorption);

    // Statistical queries
    @Query("SELECT AVG(md.avgPricePerSqft) FROM MarketData md WHERE md.city = :city AND md.state = :state")
    BigDecimal getAveragePricePerSqftByLocation(@Param("city") String city, @Param("state") String state);

    @Query("SELECT AVG(md.growthRateYoy) FROM MarketData md WHERE md.propertyType = :propertyType")
    BigDecimal getAverageGrowthRateByPropertyType(@Param("propertyType") Property.PropertyType propertyType);

    @Query("SELECT MAX(md.avgPricePerSqft) FROM MarketData md WHERE md.dataDate = :date")
    BigDecimal getMaxPricePerSqftForDate(@Param("date") LocalDate date);

    @Query("SELECT MIN(md.vacancyRate) FROM MarketData md WHERE md.city = :city")
    BigDecimal getMinVacancyRateByCity(@Param("city") String city);

    // Trend analysis queries
    @Query("SELECT md FROM MarketData md WHERE " +
           "md.location = :location AND " +
           "md.dataDate BETWEEN :startDate AND :endDate " +
           "ORDER BY md.dataDate ASC")
    List<MarketData> findMarketTrendData(@Param("location") String location,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    @Query("SELECT md FROM MarketData md WHERE " +
           "md.growthRateYoy > md.growthRateQoq AND " +
           "md.growthRateYoy > 0 " +
           "ORDER BY md.growthRateYoy DESC")
    List<MarketData> findAcceleratingGrowthMarkets();

    @Query("SELECT md FROM MarketData md WHERE " +
           "md.priceToRentRatio BETWEEN :minRatio AND :maxRatio " +
           "ORDER BY md.priceToRentRatio ASC")
    List<MarketData> findOptimalPriceToRentRatios(@Param("minRatio") BigDecimal minRatio,
                                                 @Param("maxRatio") BigDecimal maxRatio);

    // Native SQL queries for complex analytics
    @Query(value = "SELECT " +
                   "city, " +
                   "state, " +
                   "COUNT(*) as data_points, " +
                   "AVG(avg_price_per_sqft) as avg_price, " +
                   "AVG(growth_rate_yoy) as avg_growth, " +
                   "AVG(vacancy_rate) as avg_vacancy " +
                   "FROM market_data " +
                   "WHERE data_date >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR) " +
                   "GROUP BY city, state " +
                   "HAVING COUNT(*) >= :minDataPoints " +
                   "ORDER BY avg_growth DESC",
           nativeQuery = true)
    List<Object[]> getCityMarketSummary(@Param("minDataPoints") int minDataPoints);

    @Query(value = "SELECT " +
                   "property_type, " +
                   "AVG(avg_price_per_sqft) as avg_price, " +
                   "AVG(growth_rate_yoy) as avg_growth, " +
                   "AVG(rental_growth_rate) as avg_rental_growth, " +
                   "COUNT(DISTINCT location) as market_count " +
                   "FROM market_data " +
                   "WHERE data_date >= :startDate " +
                   "GROUP BY property_type " +
                   "ORDER BY avg_growth DESC",
           nativeQuery = true)
    List<Object[]> getPropertyTypeAnalysis(@Param("startDate") LocalDate startDate);

    @Query(value = "SELECT " +
                   "market_trend, " +
                   "COUNT(*) as count, " +
                   "AVG(growth_rate_yoy) as avg_growth, " +
                   "AVG(vacancy_rate) as avg_vacancy " +
                   "FROM market_data " +
                   "GROUP BY market_trend " +
                   "ORDER BY count DESC",
           nativeQuery = true)
    List<Object[]> getMarketTrendDistribution();

    @Query(value = "WITH quarterly_data AS (" +
                   "SELECT " +
                   "location, " +
                   "YEAR(data_date) as year, " +
                   "QUARTER(data_date) as quarter, " +
                   "AVG(avg_price_per_sqft) as avg_price " +
                   "FROM market_data " +
                   "WHERE location = :location " +
                   "GROUP BY location, YEAR(data_date), QUARTER(data_date) " +
                   ") " +
                   "SELECT " +
                   "year, " +
                   "quarter, " +
                   "avg_price, " +
                   "LAG(avg_price) OVER (ORDER BY year, quarter) as prev_quarter_price, " +
                   "((avg_price - LAG(avg_price) OVER (ORDER BY year, quarter)) / " +
                   "LAG(avg_price) OVER (ORDER BY year, quarter)) * 100 as quarter_growth " +
                   "FROM quarterly_data " +
                   "ORDER BY year DESC, quarter DESC " +
                   "LIMIT 8",
           nativeQuery = true)
    List<Object[]> getQuarterlyPriceTrend(@Param("location") String location);

    // Comparative analysis
    @Query(value = "SELECT " +
                   "md1.location as location1, " +
                   "md2.location as location2, " +
                   "md1.avg_price_per_sqft as price1, " +
                   "md2.avg_price_per_sqft as price2, " +
                   "ABS(md1.avg_price_per_sqft - md2.avg_price_per_sqft) as price_diff " +
                   "FROM market_data md1 " +
                   "JOIN market_data md2 ON md1.data_date = md2.data_date " +
                   "AND md1.property_type = md2.property_type " +
                   "AND md1.id < md2.id " +
                   "WHERE md1.data_date = :date " +
                   "AND md1.property_type = :propertyType " +
                   "ORDER BY price_diff ASC " +
                   "LIMIT :limit",
           nativeQuery = true)
    List<Object[]> findSimilarPricedMarkets(@Param("date") LocalDate date,
                                           @Param("propertyType") String propertyType,
                                           @Param("limit") int limit);

    // Investment opportunity analysis
    @Query(value = "SELECT " +
                   "location, " +
                   "city, " +
                   "state, " +
                   "avg_price_per_sqft, " +
                   "growth_rate_yoy, " +
                   "rental_growth_rate, " +
                   "vacancy_rate, " +
                   "((growth_rate_yoy + rental_growth_rate) / 2) - (vacancy_rate * 2) as investment_score " +
                   "FROM market_data " +
                   "WHERE data_date = (SELECT MAX(data_date) FROM market_data) " +
                   "AND growth_rate_yoy IS NOT NULL " +
                   "AND rental_growth_rate IS NOT NULL " +
                   "AND vacancy_rate IS NOT NULL " +
                   "ORDER BY investment_score DESC " +
                   "LIMIT :limit",
           nativeQuery = true)
    List<Object[]> findTopInvestmentOpportunities(@Param("limit") int limit);

    // Update operations
    @Modifying
    @Query("UPDATE MarketData md SET md.marketTrend = :trend WHERE md.id = :marketDataId")
    int updateMarketTrend(@Param("marketDataId") Long marketDataId, 
                         @Param("trend") MarketData.MarketTrend trend);

    @Modifying
    @Query("UPDATE MarketData md SET md.notes = :notes WHERE md.id = :marketDataId")
    int updateMarketDataNotes(@Param("marketDataId") Long marketDataId, @Param("notes") String notes);

    // Bulk operations
    @Modifying
    @Query("UPDATE MarketData md SET md.dataSource = :newSource WHERE md.dataSource = :oldSource")
    int bulkUpdateDataSource(@Param("oldSource") String oldSource, @Param("newSource") String newSource);

    @Modifying
    @Query("DELETE FROM MarketData md WHERE md.dataDate < :cutoffDate")
    int deleteOldMarketData(@Param("cutoffDate") LocalDate cutoffDate);

    // Soft delete
    @Modifying
    @Query("UPDATE MarketData md SET md.isDeleted = true WHERE md.id = :marketDataId")
    int softDeleteMarketData(@Param("marketDataId") Long marketDataId);

    @Query("SELECT md FROM MarketData md WHERE md.isDeleted = false")
    List<MarketData> findAllActiveMarketData();

    // Search with filters
    @Query("SELECT md FROM MarketData md WHERE " +
           "(:city IS NULL OR LOWER(md.city) = LOWER(:city)) AND " +
           "(:state IS NULL OR LOWER(md.state) = LOWER(:state)) AND " +
           "(:propertyType IS NULL OR md.propertyType = :propertyType) AND " +
           "(:marketTrend IS NULL OR md.marketTrend = :marketTrend) AND " +
           "(:minPrice IS NULL OR md.avgPricePerSqft >= :minPrice) AND " +
           "(:maxPrice IS NULL OR md.avgPricePerSqft <= :maxPrice) AND " +
           "(:startDate IS NULL OR md.dataDate >= :startDate) AND " +
           "(:endDate IS NULL OR md.dataDate <= :endDate)")
    Page<MarketData> searchMarketData(@Param("city") String city,
                                     @Param("state") String state,
                                     @Param("propertyType") Property.PropertyType propertyType,
                                     @Param("marketTrend") MarketData.MarketTrend marketTrend,
                                     @Param("minPrice") BigDecimal minPrice,
                                     @Param("maxPrice") BigDecimal maxPrice,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate,
                                     Pageable pageable);

    // Performance optimized queries
    @Query(value = "SELECT /*+ INDEX(market_data, idx_market_location_date) */ * FROM market_data " +
                   "WHERE location = :location AND data_date >= :startDate " +
                   "ORDER BY data_date DESC",
           nativeQuery = true)
    List<MarketData> findLocationDataOptimized(@Param("location") String location, 
                                              @Param("startDate") LocalDate startDate);
}