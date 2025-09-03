package com.realestate.repository;

import com.realestate.model.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Interface for custom property operations
 * Defines complex query methods that require custom implementation
 */
public interface CustomPropertyRepository {

    /**
     * Find properties with advanced search criteria using dynamic queries
     */
    List<Property> findPropertiesWithAdvancedSearch(
            Property.PropertyType propertyType,
            Property.PropertyStatus status,
            String city,
            String state,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            BigDecimal minArea,
            BigDecimal maxArea,
            Integer minBedrooms,
            Integer maxBedrooms,
            Integer minBathrooms,
            Integer maxBathrooms,
            Boolean isFurnished,
            Boolean hasGarden,
            Boolean hasPool,
            Boolean hasGarage,
            List<String> amenities,
            String sortBy,
            String sortDirection,
            int limit);

    /**
     * Find properties within a geographical radius (in kilometers)
     */
    List<Property> findPropertiesWithinRadius(Double latitude, Double longitude, Double radiusKm);

    /**
     * Find properties with similar characteristics to a given property
     */
    List<Property> findSimilarProperties(Long propertyId, int maxResults);

    /**
     * Get property market analysis for a specific location
     */
    Map<String, Object> getLocationMarketAnalysis(String city, String state, Property.PropertyType propertyType);

    /**
     * Find properties with the best price per square foot ratio
     */
    List<Property> findBestValueProperties(
            BigDecimal maxPricePerSqft,
            Property.PropertyType propertyType,
            String city,
            String state,
            int limit);

    /**
     * Get property investment potential analysis
     */
    List<Map<String, Object>> getPropertyInvestmentPotential(
            BigDecimal minROI,
            BigDecimal minRentalYield,
            String city,
            String state);

    /**
     * Find properties with high appreciation potential based on market trends
     */
    List<Property> findHighAppreciationPotentialProperties(String city, String state, int limit);

    /**
     * Get comprehensive property search with filters and pagination
     */
    Page<Property> searchPropertiesWithComplexFilters(
            Map<String, Object> filters,
            Pageable pageable);

    /**
     * Find properties suitable for rental investment
     */
    List<Property> findRentalInvestmentProperties(
            BigDecimal maxPrice,
            BigDecimal minRentalYield,
            String city,
            String state,
            int limit);

    /**
     * Get property comparison data for multiple properties
     */
    List<Map<String, Object>> compareProperties(List<Long> propertyIds);

    /**
     * Find trending properties based on view count and recent activity
     */
    List<Property> findTrendingProperties(int days, int limit);

    /**
     * Get property price history and trends
     */
    Map<String, Object> getPropertyPriceTrends(String city, String state, Property.PropertyType propertyType);

    /**
     * Find properties matching user preferences with scoring algorithm
     */
    List<Map<String, Object>> findPropertiesWithScoring(
            Map<String, Object> preferences,
            Map<String, Double> weights,
            int limit);

    /**
     * Get property market heat map data
     */
    List<Map<String, Object>> getPropertyMarketHeatMap(String state);

    /**
     * Find undervalued properties based on market comparison
     */
    List<Property> findUndervaluedProperties(
            String city,
            String state,
            Property.PropertyType propertyType,
            Double discountPercentage,
            int limit);

    /**
     * Get property inventory analysis
     */
    Map<String, Object> getPropertyInventoryAnalysis(String city, String state);
}