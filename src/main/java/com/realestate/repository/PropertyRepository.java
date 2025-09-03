package com.realestate.repository;

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
 * JPA repository for Property entity with advanced search queries
 * Provides comprehensive property search and filtering capabilities
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>, CustomPropertyRepository {

    // Derived query methods
    List<Property> findByPropertyType(Property.PropertyType propertyType);
    
    List<Property> findByStatus(Property.PropertyStatus status);
    
    List<Property> findByOwner(User owner);
    
    List<Property> findByOwnerId(Long ownerId);
    
    List<Property> findByCity(String city);
    
    List<Property> findByState(String state);
    
    List<Property> findByPincode(String pincode);
    
    List<Property> findByCityAndState(String city, String state);
    
    List<Property> findByLocationContainingIgnoreCase(String location);
    
    List<Property> findByTitleContainingIgnoreCase(String title);
    
    List<Property> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Property> findByAreaBetween(BigDecimal minArea, BigDecimal maxArea);
    
    List<Property> findByBedroomsGreaterThanEqual(Integer bedrooms);
    
    List<Property> findByBathroomsGreaterThanEqual(Integer bathrooms);
    
    List<Property> findByYearBuiltGreaterThanEqual(Integer yearBuilt);
    
    List<Property> findByStatusAndPropertyType(Property.PropertyStatus status, 
                                              Property.PropertyType propertyType);
    
    List<Property> findByIsFurnished(Boolean isFurnished);
    
    List<Property> findByHasGarden(Boolean hasGarden);
    
    List<Property> findByHasPool(Boolean hasPool);
    
    List<Property> findByHasGarage(Boolean hasGarage);
    
    boolean existsByOwnerIdAndTitle(Long ownerId, String title);
    
    long countByPropertyType(Property.PropertyType propertyType);
    
    long countByStatus(Property.PropertyStatus status);
    
    long countByOwner(User owner);

    // Custom JPQL queries
    @Query("SELECT p FROM Property p WHERE p.status = :status ORDER BY p.price ASC")
    List<Property> findAvailablePropertiesSortedByPrice(@Param("status") Property.PropertyStatus status);

    @Query("SELECT p FROM Property p WHERE p.listingDate BETWEEN :startDate AND :endDate")
    List<Property> findPropertiesListedBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Property p WHERE p.viewCount > :minViews ORDER BY p.viewCount DESC")
    List<Property> findPopularProperties(@Param("minViews") Long minViews);

    @Query("SELECT p FROM Property p WHERE p.monthlyRental IS NOT NULL AND p.price / (p.monthlyRental * 12) < :maxRatio")
    List<Property> findPropertiesWithGoodRentalYield(@Param("maxRatio") BigDecimal maxRatio);

    @Query("SELECT p FROM Property p WHERE SIZE(p.amenities) >= :minAmenities")
    List<Property> findPropertiesWithMinimumAmenities(@Param("minAmenities") int minAmenities);

    @Query("SELECT p FROM Property p WHERE :amenity MEMBER OF p.amenities")
    List<Property> findPropertiesByAmenity(@Param("amenity") String amenity);

    @Query("SELECT p FROM Property p WHERE p.latitude IS NOT NULL AND p.longitude IS NOT NULL")
    List<Property> findPropertiesWithCoordinates();

    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.owner WHERE p.id = :propertyId")
    Optional<Property> findPropertyWithOwner(@Param("propertyId") Long propertyId);

    @Query("SELECT p FROM Property p LEFT JOIN FETCH p.calculations WHERE p.id = :propertyId")
    Optional<Property> findPropertyWithCalculations(@Param("propertyId") Long propertyId);

    // Complex search query with multiple criteria
    @Query("SELECT p FROM Property p WHERE " +
           "(:propertyType IS NULL OR p.propertyType = :propertyType) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:city IS NULL OR LOWER(p.city) = LOWER(:city)) AND " +
           "(:state IS NULL OR LOWER(p.state) = LOWER(:state)) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:minArea IS NULL OR p.area >= :minArea) AND " +
           "(:maxArea IS NULL OR p.area <= :maxArea) AND " +
           "(:minBedrooms IS NULL OR p.bedrooms >= :minBedrooms) AND " +
           "(:minBathrooms IS NULL OR p.bathrooms >= :minBathrooms) AND " +
           "(:isFurnished IS NULL OR p.isFurnished = :isFurnished)")
    Page<Property> searchProperties(@Param("propertyType") Property.PropertyType propertyType,
                                   @Param("status") Property.PropertyStatus status,
                                   @Param("city") String city,
                                   @Param("state") String state,
                                   @Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice,
                                   @Param("minArea") BigDecimal minArea,
                                   @Param("maxArea") BigDecimal maxArea,
                                   @Param("minBedrooms") Integer minBedrooms,
                                   @Param("minBathrooms") Integer minBathrooms,
                                   @Param("isFurnished") Boolean isFurnished,
                                   Pageable pageable);

    // Geospatial queries (simplified distance calculation)
    @Query("SELECT p FROM Property p WHERE " +
           "p.latitude IS NOT NULL AND p.longitude IS NOT NULL AND " +
           "ABS(p.latitude - :lat) <= :latRange AND " +
           "ABS(p.longitude - :lon) <= :lonRange")
    List<Property> findPropertiesNearLocation(@Param("lat") Double latitude,
                                             @Param("lon") Double longitude,
                                             @Param("latRange") Double latRange,
                                             @Param("lonRange") Double lonRange);

    // Native SQL queries for complex analytics
    @Query(value = "SELECT p.property_type, " +
                   "COUNT(*) as count, " +
                   "AVG(p.price) as avg_price, " +
                   "MIN(p.price) as min_price, " +
                   "MAX(p.price) as max_price " +
                   "FROM properties p " +
                   "WHERE p.status = :status " +
                   "GROUP BY p.property_type " +
                   "ORDER BY avg_price DESC",
           nativeQuery = true)
    List<Object[]> getPropertyStatisticsByType(@Param("status") String status);

    @Query(value = "SELECT p.city, p.state, " +
                   "COUNT(*) as property_count, " +
                   "AVG(p.price) as avg_price, " +
                   "AVG(p.area) as avg_area " +
                   "FROM properties p " +
                   "WHERE p.city IS NOT NULL AND p.state IS NOT NULL " +
                   "GROUP BY p.city, p.state " +
                   "HAVING COUNT(*) >= :minCount " +
                   "ORDER BY property_count DESC",
           nativeQuery = true)
    List<Object[]> getPropertyStatisticsByLocation(@Param("minCount") int minCount);

    @Query(value = "SELECT * FROM properties p " +
                   "WHERE p.price BETWEEN :minPrice AND :maxPrice " +
                   "AND p.area BETWEEN :minArea AND :maxArea " +
                   "AND (:bedrooms IS NULL OR p.bedrooms >= :bedrooms) " +
                   "AND (:propertyType IS NULL OR p.property_type = :propertyType) " +
                   "ORDER BY (p.price / p.area) ASC",
           nativeQuery = true)
    List<Property> findBestValueProperties(@Param("minPrice") BigDecimal minPrice,
                                          @Param("maxPrice") BigDecimal maxPrice,
                                          @Param("minArea") BigDecimal minArea,
                                          @Param("maxArea") BigDecimal maxArea,
                                          @Param("bedrooms") Integer bedrooms,
                                          @Param("propertyType") String propertyType);

    // Performance optimized queries
    @Query(value = "SELECT /*+ INDEX(properties, idx_property_location) */ * FROM properties " +
                   "WHERE city = :city AND state = :state",
           nativeQuery = true)
    List<Property> findPropertiesByCityStateOptimized(@Param("city") String city, 
                                                     @Param("state") String state);

    @Query(value = "SELECT /*+ INDEX(properties, idx_property_price) */ * FROM properties " +
                   "WHERE price BETWEEN :minPrice AND :maxPrice " +
                   "ORDER BY price ASC",
           nativeQuery = true)
    List<Property> findPropertiesInPriceRangeOptimized(@Param("minPrice") BigDecimal minPrice,
                                                      @Param("maxPrice") BigDecimal maxPrice);

    // Update operations
    @Modifying
    @Query("UPDATE Property p SET p.viewCount = p.viewCount + 1 WHERE p.id = :propertyId")
    int incrementViewCount(@Param("propertyId") Long propertyId);

    @Modifying
    @Query("UPDATE Property p SET p.status = :status WHERE p.id = :propertyId")
    int updatePropertyStatus(@Param("propertyId") Long propertyId, 
                           @Param("status") Property.PropertyStatus status);

    @Modifying
    @Query("UPDATE Property p SET p.status = :status, p.soldDate = :soldDate WHERE p.id = :propertyId")
    int markPropertyAsSold(@Param("propertyId") Long propertyId,
                          @Param("status") Property.PropertyStatus status,
                          @Param("soldDate") LocalDateTime soldDate);

    @Modifying
    @Query("UPDATE Property p SET p.monthlyRental = :rental WHERE p.id = :propertyId")
    int updateMonthlyRental(@Param("propertyId") Long propertyId, 
                          @Param("rental") BigDecimal rental);

    // Bulk operations
    @Modifying
    @Query("UPDATE Property p SET p.status = :newStatus WHERE p.status = :oldStatus AND p.owner = :owner")
    int bulkUpdatePropertyStatusByOwner(@Param("owner") User owner,
                                       @Param("oldStatus") Property.PropertyStatus oldStatus,
                                       @Param("newStatus") Property.PropertyStatus newStatus);

    // Soft delete
    @Modifying
    @Query("UPDATE Property p SET p.isDeleted = true WHERE p.id = :propertyId")
    int softDeleteProperty(@Param("propertyId") Long propertyId);

    @Query("SELECT p FROM Property p WHERE p.isDeleted = false")
    List<Property> findAllActiveProperties();

    // Text search capabilities
    @Query("SELECT p FROM Property p WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Property> searchPropertiesByText(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Recent listings
    @Query("SELECT p FROM Property p WHERE p.listingDate >= :since ORDER BY p.listingDate DESC")
    List<Property> findRecentListings(@Param("since") LocalDateTime since);

    // Price range analysis
    @Query(value = "SELECT " +
                   "CASE " +
                   "WHEN price < 1000000 THEN 'Under 10L' " +
                   "WHEN price < 5000000 THEN '10L-50L' " +
                   "WHEN price < 10000000 THEN '50L-1Cr' " +
                   "ELSE 'Above 1Cr' " +
                   "END as price_range, " +
                   "COUNT(*) as count " +
                   "FROM properties " +
                   "WHERE status = 'AVAILABLE' " +
                   "GROUP BY price_range " +
                   "ORDER BY count DESC",
           nativeQuery = true)
    List<Object[]> getPriceRangeDistribution();
}