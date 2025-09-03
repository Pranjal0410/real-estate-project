package com.realestate.repository;

import com.realestate.model.entity.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

/**
 * JDBC implementation for complex property operations using JdbcTemplate
 * Provides raw SQL operations for performance-critical queries
 */
@Repository
public class PropertyJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Property Row Mapper
    private static final RowMapper<Property> PROPERTY_ROW_MAPPER = new RowMapper<Property>() {
        @Override
        public Property mapRow(ResultSet rs, int rowNum) throws SQLException {
            Property property = Property.builder()
                    .title(rs.getString("title"))
                    .price(rs.getBigDecimal("price"))
                    .location(rs.getString("location"))
                    .city(rs.getString("city"))
                    .state(rs.getString("state"))
                    .pincode(rs.getString("pincode"))
                    .propertyType(Property.PropertyType.valueOf(rs.getString("property_type")))
                    .area(rs.getBigDecimal("area"))
                    .areaUnit(rs.getString("area_unit") != null ? 
                             Property.AreaUnit.valueOf(rs.getString("area_unit")) : null)
                    .bedrooms(rs.getInt("bedrooms"))
                    .bathrooms(rs.getInt("bathrooms"))
                    .parkingSpaces(rs.getInt("parking_spaces"))
                    .yearBuilt(rs.getInt("year_built"))
                    .description(rs.getString("description"))
                    .status(Property.PropertyStatus.valueOf(rs.getString("status")))
                    .isFurnished(rs.getBoolean("is_furnished"))
                    .hasGarden(rs.getBoolean("has_garden"))
                    .hasPool(rs.getBoolean("has_pool"))
                    .hasGarage(rs.getBoolean("has_garage"))
                    .monthlyRental(rs.getBigDecimal("monthly_rental"))
                    .annualMaintenance(rs.getBigDecimal("annual_maintenance"))
                    .propertyTax(rs.getBigDecimal("property_tax"))
                    .hoaFees(rs.getBigDecimal("hoa_fees"))
                    .insuranceCost(rs.getBigDecimal("insurance_cost"))
                    .listingDate(rs.getTimestamp("listing_date") != null ? 
                               rs.getTimestamp("listing_date").toLocalDateTime() : null)
                    .soldDate(rs.getTimestamp("sold_date") != null ? 
                            rs.getTimestamp("sold_date").toLocalDateTime() : null)
                    .viewCount(rs.getLong("view_count"))
                    .latitude(rs.getDouble("latitude"))
                    .longitude(rs.getDouble("longitude"))
                    .build();

            // Set base entity fields
            property.setId(rs.getLong("id"));
            property.setCreatedAt(rs.getTimestamp("created_at") != null ? 
                                rs.getTimestamp("created_at").toLocalDateTime() : null);
            property.setUpdatedAt(rs.getTimestamp("updated_at") != null ? 
                                rs.getTimestamp("updated_at").toLocalDateTime() : null);
            property.setIsDeleted(rs.getBoolean("is_deleted"));
            property.setVersion(rs.getLong("version"));

            return property;
        }
    };

    /**
     * Execute complex property search with dynamic SQL building
     */
    public List<Property> executeComplexPropertySearch(Map<String, Object> searchCriteria) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.*, u.username as owner_username 
            FROM properties p 
            LEFT JOIN users u ON p.owner_id = u.id 
            WHERE p.is_deleted = false
            """);

        List<Object> parameters = new ArrayList<>();

        // Dynamic WHERE clause building
        if (searchCriteria.containsKey("propertyType")) {
            sql.append(" AND p.property_type = ?");
            parameters.add(searchCriteria.get("propertyType"));
        }

        if (searchCriteria.containsKey("minPrice")) {
            sql.append(" AND p.price >= ?");
            parameters.add(searchCriteria.get("minPrice"));
        }

        if (searchCriteria.containsKey("maxPrice")) {
            sql.append(" AND p.price <= ?");
            parameters.add(searchCriteria.get("maxPrice"));
        }

        if (searchCriteria.containsKey("city")) {
            sql.append(" AND LOWER(p.city) LIKE LOWER(?)");
            parameters.add("%" + searchCriteria.get("city") + "%");
        }

        if (searchCriteria.containsKey("state")) {
            sql.append(" AND LOWER(p.state) = LOWER(?)");
            parameters.add(searchCriteria.get("state"));
        }

        if (searchCriteria.containsKey("minBedrooms")) {
            sql.append(" AND p.bedrooms >= ?");
            parameters.add(searchCriteria.get("minBedrooms"));
        }

        if (searchCriteria.containsKey("minArea")) {
            sql.append(" AND p.area >= ?");
            parameters.add(searchCriteria.get("minArea"));
        }

        if (searchCriteria.containsKey("maxArea")) {
            sql.append(" AND p.area <= ?");
            parameters.add(searchCriteria.get("maxArea"));
        }

        if (searchCriteria.containsKey("status")) {
            sql.append(" AND p.status = ?");
            parameters.add(searchCriteria.get("status"));
        }

        if (searchCriteria.containsKey("hasAmenities")) {
            @SuppressWarnings("unchecked")
            List<String> amenities = (List<String>) searchCriteria.get("hasAmenities");
            if (!amenities.isEmpty()) {
                sql.append(" AND p.id IN (SELECT pa.property_id FROM property_amenities pa WHERE pa.amenity IN (");
                sql.append("?".repeat(amenities.size()).replaceAll(".(?!$)", "?,"));
                sql.append(") GROUP BY pa.property_id HAVING COUNT(DISTINCT pa.amenity) = ?)");
                parameters.addAll(amenities);
                parameters.add(amenities.size());
            }
        }

        // Sorting
        String sortBy = (String) searchCriteria.getOrDefault("sortBy", "created_at");
        String sortDirection = (String) searchCriteria.getOrDefault("sortDirection", "DESC");
        sql.append(" ORDER BY p.").append(sortBy).append(" ").append(sortDirection);

        // Pagination
        if (searchCriteria.containsKey("limit")) {
            sql.append(" LIMIT ?");
            parameters.add(searchCriteria.get("limit"));
        }

        if (searchCriteria.containsKey("offset")) {
            sql.append(" OFFSET ?");
            parameters.add(searchCriteria.get("offset"));
        }

        return jdbcTemplate.query(sql.toString(), parameters.toArray(), PROPERTY_ROW_MAPPER);
    }

    /**
     * Get property analytics data using raw SQL
     */
    public Map<String, Object> getPropertyAnalytics(String city, String state) {
        Map<String, Object> analytics = new HashMap<>();

        // Total properties count
        String countSql = "SELECT COUNT(*) FROM properties WHERE city = ? AND state = ? AND is_deleted = false";
        Integer totalProperties = jdbcTemplate.queryForObject(countSql, Integer.class, city, state);

        // Average price
        String avgPriceSql = "SELECT AVG(price) FROM properties WHERE city = ? AND state = ? AND is_deleted = false";
        BigDecimal avgPrice = jdbcTemplate.queryForObject(avgPriceSql, BigDecimal.class, city, state);

        // Price per sqft statistics
        String pricePerSqftSql = """
            SELECT 
                AVG(price / area) as avg_price_per_sqft,
                MIN(price / area) as min_price_per_sqft,
                MAX(price / area) as max_price_per_sqft
            FROM properties 
            WHERE city = ? AND state = ? AND area > 0 AND is_deleted = false
            """;

        Map<String, Object> pricePerSqftStats = jdbcTemplate.queryForMap(pricePerSqftSql, city, state);

        // Property type distribution
        String typeDistSql = """
            SELECT property_type, COUNT(*) as count, AVG(price) as avg_price
            FROM properties 
            WHERE city = ? AND state = ? AND is_deleted = false
            GROUP BY property_type
            ORDER BY count DESC
            """;

        List<Map<String, Object>> typeDistribution = jdbcTemplate.queryForList(typeDistSql, city, state);

        // Status distribution
        String statusDistSql = """
            SELECT status, COUNT(*) as count
            FROM properties 
            WHERE city = ? AND state = ? AND is_deleted = false
            GROUP BY status
            """;

        List<Map<String, Object>> statusDistribution = jdbcTemplate.queryForList(statusDistSql, city, state);

        analytics.put("totalProperties", totalProperties);
        analytics.put("averagePrice", avgPrice);
        analytics.put("pricePerSqftStats", pricePerSqftStats);
        analytics.put("propertyTypeDistribution", typeDistribution);
        analytics.put("statusDistribution", statusDistribution);
        analytics.put("city", city);
        analytics.put("state", state);

        return analytics;
    }

    /**
     * Batch update property prices based on market trends
     */
    public int batchUpdatePropertyPrices(String city, String state, BigDecimal adjustmentPercentage) {
        String sql = """
            UPDATE properties 
            SET price = price * (1 + ? / 100),
                updated_at = CURRENT_TIMESTAMP
            WHERE city = ? AND state = ? 
            AND status = 'AVAILABLE' 
            AND is_deleted = false
            """;

        return jdbcTemplate.update(sql, adjustmentPercentage, city, state);
    }

    /**
     * Get property investment ROI calculations
     */
    public List<Map<String, Object>> getPropertyInvestmentROI(String city, String state, int limit) {
        String sql = """
            SELECT 
                p.id,
                p.title,
                p.price,
                p.monthly_rental,
                p.area,
                CASE 
                    WHEN p.monthly_rental IS NOT NULL AND p.price > 0 
                    THEN (p.monthly_rental * 12 / p.price * 100)
                    ELSE 0 
                END as annual_rental_yield,
                CASE 
                    WHEN p.area > 0 
                    THEN (p.price / p.area)
                    ELSE 0 
                END as price_per_sqft,
                p.view_count,
                DATEDIFF(CURRENT_DATE, DATE(p.listing_date)) as days_on_market
            FROM properties p
            WHERE p.city = ? 
            AND p.state = ?
            AND p.status = 'AVAILABLE'
            AND p.is_deleted = false
            AND p.monthly_rental IS NOT NULL
            AND p.price > 0
            ORDER BY annual_rental_yield DESC
            LIMIT ?
            """;

        return jdbcTemplate.queryForList(sql, city, state, limit);
    }

    /**
     * Find properties within geographical bounds using spatial query
     */
    public List<Property> findPropertiesInBounds(double minLat, double maxLat, double minLon, double maxLon) {
        String sql = """
            SELECT * FROM properties 
            WHERE latitude BETWEEN ? AND ?
            AND longitude BETWEEN ? AND ?
            AND latitude IS NOT NULL 
            AND longitude IS NOT NULL
            AND is_deleted = false
            ORDER BY 
                (POW(latitude - ?, 2) + POW(longitude - ?, 2)) ASC
            """;

        double centerLat = (minLat + maxLat) / 2;
        double centerLon = (minLon + maxLon) / 2;

        return jdbcTemplate.query(sql, PROPERTY_ROW_MAPPER, 
                                minLat, maxLat, minLon, maxLon, centerLat, centerLon);
    }

    /**
     * Get monthly property listing trends
     */
    public List<Map<String, Object>> getMonthlyListingTrends(int months) {
        String sql = """
            SELECT 
                DATE_FORMAT(listing_date, '%Y-%m') as month,
                COUNT(*) as listings_count,
                AVG(price) as avg_price,
                AVG(area) as avg_area,
                AVG(CASE WHEN area > 0 THEN price/area ELSE NULL END) as avg_price_per_sqft
            FROM properties
            WHERE listing_date >= DATE_SUB(CURRENT_DATE, INTERVAL ? MONTH)
            AND is_deleted = false
            GROUP BY DATE_FORMAT(listing_date, '%Y-%m')
            ORDER BY month DESC
            """;

        return jdbcTemplate.queryForList(sql, months);
    }

    /**
     * Execute stored procedure for complex property matching algorithm
     */
    public List<Property> executePropertyMatchingProcedure(Long userId, Map<String, Object> preferences) {
        String sql = "CALL find_matching_properties(?, ?, ?, ?, ?, ?)";
        
        // Extract preferences with defaults
        String preferredCity = (String) preferences.getOrDefault("city", null);
        String preferredPropertyType = (String) preferences.getOrDefault("propertyType", null);
        BigDecimal maxBudget = (BigDecimal) preferences.getOrDefault("maxBudget", null);
        Integer minBedrooms = (Integer) preferences.getOrDefault("minBedrooms", null);
        Integer limit = (Integer) preferences.getOrDefault("limit", 20);

        try {
            return jdbcTemplate.query(sql, PROPERTY_ROW_MAPPER, 
                                    userId, preferredCity, preferredPropertyType, 
                                    maxBudget, minBedrooms, limit);
        } catch (Exception e) {
            // If stored procedure doesn't exist, fall back to regular query
            return findPropertiesByPreferences(preferences, limit);
        }
    }

    /**
     * Bulk insert property amenities
     */
    public void bulkInsertPropertyAmenities(Long propertyId, Set<String> amenities) {
        // First delete existing amenities
        String deleteSql = "DELETE FROM property_amenities WHERE property_id = ?";
        jdbcTemplate.update(deleteSql, propertyId);

        if (!amenities.isEmpty()) {
            String insertSql = "INSERT INTO property_amenities (property_id, amenity) VALUES (?, ?)";
            
            List<Object[]> batchArgs = amenities.stream()
                    .map(amenity -> new Object[]{propertyId, amenity})
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);

            jdbcTemplate.batchUpdate(insertSql, batchArgs);
        }
    }

    /**
     * Bulk insert property images
     */
    public void bulkInsertPropertyImages(Long propertyId, Set<String> imageUrls) {
        // First delete existing images
        String deleteSql = "DELETE FROM property_images WHERE property_id = ?";
        jdbcTemplate.update(deleteSql, propertyId);

        if (!imageUrls.isEmpty()) {
            String insertSql = "INSERT INTO property_images (property_id, image_url) VALUES (?, ?)";
            
            List<Object[]> batchArgs = imageUrls.stream()
                    .map(imageUrl -> new Object[]{propertyId, imageUrl})
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);

            jdbcTemplate.batchUpdate(insertSql, batchArgs);
        }
    }

    /**
     * Get property details with all related data using joins
     */
    public Optional<Map<String, Object>> getPropertyWithFullDetails(Long propertyId) {
        String sql = """
            SELECT 
                p.*,
                u.username as owner_username,
                u.email as owner_email,
                u.first_name as owner_first_name,
                u.last_name as owner_last_name,
                COUNT(DISTINCT pa.amenity) as amenity_count,
                COUNT(DISTINCT pi.image_url) as image_count,
                COUNT(DISTINCT ic.id) as calculation_count,
                AVG(ic.roi_percentage) as avg_roi
            FROM properties p
            LEFT JOIN users u ON p.owner_id = u.id
            LEFT JOIN property_amenities pa ON p.id = pa.property_id
            LEFT JOIN property_images pi ON p.id = pi.property_id
            LEFT JOIN investment_calculations ic ON p.id = ic.property_id
            WHERE p.id = ? AND p.is_deleted = false
            GROUP BY p.id, u.username, u.email, u.first_name, u.last_name
            """;

        try {
            Map<String, Object> propertyDetails = jdbcTemplate.queryForMap(sql, propertyId);
            
            // Get amenities separately
            String amenitiesSql = "SELECT amenity FROM property_amenities WHERE property_id = ?";
            List<String> amenities = jdbcTemplate.queryForList(amenitiesSql, String.class, propertyId);
            propertyDetails.put("amenities", amenities);

            // Get images separately
            String imagesSql = "SELECT image_url FROM property_images WHERE property_id = ?";
            List<String> images = jdbcTemplate.queryForList(imagesSql, String.class, propertyId);
            propertyDetails.put("images", images);

            return Optional.of(propertyDetails);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Execute property cleanup operations
     */
    public Map<String, Integer> executePropertyCleanup() {
        Map<String, Integer> results = new HashMap<>();

        // Remove orphaned amenities
        String cleanAmenitiesSql = """
            DELETE FROM property_amenities 
            WHERE property_id NOT IN (SELECT id FROM properties WHERE is_deleted = false)
            """;
        int orphanedAmenities = jdbcTemplate.update(cleanAmenitiesSql);

        // Remove orphaned images
        String cleanImagesSql = """
            DELETE FROM property_images 
            WHERE property_id NOT IN (SELECT id FROM properties WHERE is_deleted = false)
            """;
        int orphanedImages = jdbcTemplate.update(cleanImagesSql);

        // Reset view counts for old listings
        String resetViewCountsSql = """
            UPDATE properties 
            SET view_count = 0, updated_at = CURRENT_TIMESTAMP
            WHERE listing_date < DATE_SUB(CURRENT_DATE, INTERVAL 1 YEAR)
            AND view_count > 1000
            AND is_deleted = false
            """;
        int resetViewCounts = jdbcTemplate.update(resetViewCountsSql);

        results.put("orphanedAmenities", orphanedAmenities);
        results.put("orphanedImages", orphanedImages);
        results.put("resetViewCounts", resetViewCounts);

        return results;
    }

    /**
     * Get property performance metrics
     */
    public List<Map<String, Object>> getPropertyPerformanceMetrics(String timeFrame) {
        String sql = switch (timeFrame.toLowerCase()) {
            case "weekly" -> """
                SELECT 
                    DATE(DATE_SUB(listing_date, INTERVAL WEEKDAY(listing_date) DAY)) as period,
                    COUNT(*) as listings,
                    AVG(view_count) as avg_views,
                    AVG(price) as avg_price,
                    COUNT(CASE WHEN status = 'SOLD' THEN 1 END) as sold_count
                FROM properties
                WHERE listing_date >= DATE_SUB(CURRENT_DATE, INTERVAL 12 WEEK)
                AND is_deleted = false
                GROUP BY DATE(DATE_SUB(listing_date, INTERVAL WEEKDAY(listing_date) DAY))
                ORDER BY period DESC
                """;
            case "monthly" -> """
                SELECT 
                    DATE_FORMAT(listing_date, '%Y-%m') as period,
                    COUNT(*) as listings,
                    AVG(view_count) as avg_views,
                    AVG(price) as avg_price,
                    COUNT(CASE WHEN status = 'SOLD' THEN 1 END) as sold_count
                FROM properties
                WHERE listing_date >= DATE_SUB(CURRENT_DATE, INTERVAL 12 MONTH)
                AND is_deleted = false
                GROUP BY DATE_FORMAT(listing_date, '%Y-%m')
                ORDER BY period DESC
                """;
            default -> """
                SELECT 
                    DATE(listing_date) as period,
                    COUNT(*) as listings,
                    AVG(view_count) as avg_views,
                    AVG(price) as avg_price,
                    COUNT(CASE WHEN status = 'SOLD' THEN 1 END) as sold_count
                FROM properties
                WHERE listing_date >= DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY)
                AND is_deleted = false
                GROUP BY DATE(listing_date)
                ORDER BY period DESC
                """;
        };

        return jdbcTemplate.queryForList(sql);
    }

    // Helper method for property preferences fallback
    private List<Property> findPropertiesByPreferences(Map<String, Object> preferences, Integer limit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM properties WHERE is_deleted = false");
        List<Object> parameters = new ArrayList<>();

        if (preferences.containsKey("city")) {
            sql.append(" AND city = ?");
            parameters.add(preferences.get("city"));
        }

        if (preferences.containsKey("propertyType")) {
            sql.append(" AND property_type = ?");
            parameters.add(preferences.get("propertyType"));
        }

        if (preferences.containsKey("maxBudget")) {
            sql.append(" AND price <= ?");
            parameters.add(preferences.get("maxBudget"));
        }

        if (preferences.containsKey("minBedrooms")) {
            sql.append(" AND bedrooms >= ?");
            parameters.add(preferences.get("minBedrooms"));
        }

        sql.append(" ORDER BY view_count DESC, listing_date DESC");

        if (limit != null) {
            sql.append(" LIMIT ?");
            parameters.add(limit);
        }

        return jdbcTemplate.query(sql.toString(), parameters.toArray(), PROPERTY_ROW_MAPPER);
    }
}