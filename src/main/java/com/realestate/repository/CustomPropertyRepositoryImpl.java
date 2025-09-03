package com.realestate.repository;

import com.realestate.model.entity.Property;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation using EntityManager for complex queries
 * Provides custom query implementations for advanced property operations
 */
@Repository
public class CustomPropertyRepositoryImpl implements CustomPropertyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Property> findPropertiesWithAdvancedSearch(
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
            int limit) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> query = cb.createQuery(Property.class);
        Root<Property> property = query.from(Property.class);

        List<Predicate> predicates = new ArrayList<>();

        // Add filters dynamically
        if (propertyType != null) {
            predicates.add(cb.equal(property.get("propertyType"), propertyType));
        }
        if (status != null) {
            predicates.add(cb.equal(property.get("status"), status));
        }
        if (city != null && !city.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(property.get("city")), 
                         cb.lower(cb.literal("%" + city + "%"))));
        }
        if (state != null && !state.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(property.get("state")), 
                         cb.lower(cb.literal("%" + state + "%"))));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(property.get("price"), maxPrice));
        }
        if (minArea != null) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("area"), minArea));
        }
        if (maxArea != null) {
            predicates.add(cb.lessThanOrEqualTo(property.get("area"), maxArea));
        }
        if (minBedrooms != null) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("bedrooms"), minBedrooms));
        }
        if (maxBedrooms != null) {
            predicates.add(cb.lessThanOrEqualTo(property.get("bedrooms"), maxBedrooms));
        }
        if (minBathrooms != null) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("bathrooms"), minBathrooms));
        }
        if (maxBathrooms != null) {
            predicates.add(cb.lessThanOrEqualTo(property.get("bathrooms"), maxBathrooms));
        }
        if (isFurnished != null) {
            predicates.add(cb.equal(property.get("isFurnished"), isFurnished));
        }
        if (hasGarden != null) {
            predicates.add(cb.equal(property.get("hasGarden"), hasGarden));
        }
        if (hasPool != null) {
            predicates.add(cb.equal(property.get("hasPool"), hasPool));
        }
        if (hasGarage != null) {
            predicates.add(cb.equal(property.get("hasGarage"), hasGarage));
        }

        // Amenities filter
        if (amenities != null && !amenities.isEmpty()) {
            for (String amenity : amenities) {
                predicates.add(cb.isMember(amenity, property.get("amenities")));
            }
        }

        // Apply soft delete filter
        predicates.add(cb.equal(property.get("isDeleted"), false));

        query.where(predicates.toArray(new Predicate[0]));

        // Add sorting
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Order order;
            if ("desc".equalsIgnoreCase(sortDirection)) {
                order = cb.desc(property.get(sortBy));
            } else {
                order = cb.asc(property.get(sortBy));
            }
            query.orderBy(order);
        }

        TypedQuery<Property> typedQuery = entityManager.createQuery(query);
        if (limit > 0) {
            typedQuery.setMaxResults(limit);
        }

        return typedQuery.getResultList();
    }

    @Override
    public List<Property> findPropertiesWithinRadius(Double latitude, Double longitude, Double radiusKm) {
        String sql = """
            SELECT p FROM Property p 
            WHERE p.latitude IS NOT NULL 
            AND p.longitude IS NOT NULL 
            AND p.isDeleted = false
            AND (
                6371 * acos(
                    cos(radians(:latitude)) * 
                    cos(radians(p.latitude)) * 
                    cos(radians(p.longitude) - radians(:longitude)) + 
                    sin(radians(:latitude)) * 
                    sin(radians(p.latitude))
                )
            ) <= :radius
            ORDER BY (
                6371 * acos(
                    cos(radians(:latitude)) * 
                    cos(radians(p.latitude)) * 
                    cos(radians(p.longitude) - radians(:longitude)) + 
                    sin(radians(:latitude)) * 
                    sin(radians(p.latitude))
                )
            ) ASC
            """;

        TypedQuery<Property> query = entityManager.createQuery(sql, Property.class);
        query.setParameter("latitude", latitude);
        query.setParameter("longitude", longitude);
        query.setParameter("radius", radiusKm);

        return query.getResultList();
    }

    @Override
    public List<Property> findSimilarProperties(Long propertyId, int maxResults) {
        // First get the reference property
        Property referenceProperty = entityManager.find(Property.class, propertyId);
        if (referenceProperty == null) {
            return new ArrayList<>();
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> query = cb.createQuery(Property.class);
        Root<Property> property = query.from(Property.class);

        List<Predicate> predicates = new ArrayList<>();

        // Exclude the reference property itself
        predicates.add(cb.notEqual(property.get("id"), propertyId));

        // Similar property type
        predicates.add(cb.equal(property.get("propertyType"), referenceProperty.getPropertyType()));

        // Similar location (city and state)
        if (referenceProperty.getCity() != null) {
            predicates.add(cb.equal(property.get("city"), referenceProperty.getCity()));
        }
        if (referenceProperty.getState() != null) {
            predicates.add(cb.equal(property.get("state"), referenceProperty.getState()));
        }

        // Price range (±30% of reference price)
        BigDecimal refPrice = referenceProperty.getPrice();
        BigDecimal priceVariation = refPrice.multiply(BigDecimal.valueOf(0.3));
        predicates.add(cb.between(property.get("price"), 
                                refPrice.subtract(priceVariation), 
                                refPrice.add(priceVariation)));

        // Similar area (±25% of reference area)
        BigDecimal refArea = referenceProperty.getArea();
        BigDecimal areaVariation = refArea.multiply(BigDecimal.valueOf(0.25));
        predicates.add(cb.between(property.get("area"), 
                                refArea.subtract(areaVariation), 
                                refArea.add(areaVariation)));

        // Similar bedrooms (±1 bedroom)
        if (referenceProperty.getBedrooms() != null) {
            predicates.add(cb.between(property.get("bedrooms"), 
                                    Math.max(0, referenceProperty.getBedrooms() - 1), 
                                    referenceProperty.getBedrooms() + 1));
        }

        // Soft delete filter
        predicates.add(cb.equal(property.get("isDeleted"), false));

        query.where(predicates.toArray(new Predicate[0]));

        // Order by price similarity
        Expression<BigDecimal> priceDifference = cb.abs(cb.diff(property.get("price"), refPrice));
        query.orderBy(cb.asc(priceDifference));

        TypedQuery<Property> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(maxResults);

        return typedQuery.getResultList();
    }

    @Override
    public Map<String, Object> getLocationMarketAnalysis(String city, String state, Property.PropertyType propertyType) {
        Map<String, Object> analysis = new HashMap<>();

        // Get property count
        String countSql = """
            SELECT COUNT(p) FROM Property p 
            WHERE p.city = :city AND p.state = :state 
            AND (:propertyType IS NULL OR p.propertyType = :propertyType)
            AND p.isDeleted = false
            """;
        
        Long propertyCount = entityManager.createQuery(countSql, Long.class)
                .setParameter("city", city)
                .setParameter("state", state)
                .setParameter("propertyType", propertyType)
                .getSingleResult();

        // Get average price
        String avgPriceSql = """
            SELECT AVG(p.price) FROM Property p 
            WHERE p.city = :city AND p.state = :state 
            AND (:propertyType IS NULL OR p.propertyType = :propertyType)
            AND p.isDeleted = false
            """;
        
        BigDecimal avgPrice = entityManager.createQuery(avgPriceSql, BigDecimal.class)
                .setParameter("city", city)
                .setParameter("state", state)
                .setParameter("propertyType", propertyType)
                .getSingleResult();

        // Get price per sqft
        String avgPricePerSqftSql = """
            SELECT AVG(p.price / p.area) FROM Property p 
            WHERE p.city = :city AND p.state = :state 
            AND (:propertyType IS NULL OR p.propertyType = :propertyType)
            AND p.area > 0 AND p.isDeleted = false
            """;
        
        BigDecimal avgPricePerSqft = entityManager.createQuery(avgPricePerSqftSql, BigDecimal.class)
                .setParameter("city", city)
                .setParameter("state", state)
                .setParameter("propertyType", propertyType)
                .getSingleResult();

        // Get status distribution
        String statusSql = """
            SELECT p.status, COUNT(p) FROM Property p 
            WHERE p.city = :city AND p.state = :state 
            AND (:propertyType IS NULL OR p.propertyType = :propertyType)
            AND p.isDeleted = false
            GROUP BY p.status
            """;
        
        List<Object[]> statusResults = entityManager.createQuery(statusSql, Object[].class)
                .setParameter("city", city)
                .setParameter("state", state)
                .setParameter("propertyType", propertyType)
                .getResultList();

        Map<String, Long> statusDistribution = statusResults.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]
                ));

        analysis.put("propertyCount", propertyCount);
        analysis.put("averagePrice", avgPrice);
        analysis.put("averagePricePerSqft", avgPricePerSqft);
        analysis.put("statusDistribution", statusDistribution);
        analysis.put("city", city);
        analysis.put("state", state);
        analysis.put("propertyType", propertyType);

        return analysis;
    }

    @Override
    public List<Property> findBestValueProperties(
            BigDecimal maxPricePerSqft, 
            Property.PropertyType propertyType,
            String city, 
            String state, 
            int limit) {

        String jpql = """
            SELECT p FROM Property p 
            WHERE p.area > 0 
            AND (p.price / p.area) <= :maxPricePerSqft
            AND (:propertyType IS NULL OR p.propertyType = :propertyType)
            AND (:city IS NULL OR p.city = :city)
            AND (:state IS NULL OR p.state = :state)
            AND p.status = com.realestate.model.entity.Property$PropertyStatus.AVAILABLE
            AND p.isDeleted = false
            ORDER BY (p.price / p.area) ASC
            """;

        TypedQuery<Property> query = entityManager.createQuery(jpql, Property.class);
        query.setParameter("maxPricePerSqft", maxPricePerSqft);
        query.setParameter("propertyType", propertyType);
        query.setParameter("city", city);
        query.setParameter("state", state);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> getPropertyInvestmentPotential(
            BigDecimal minROI, 
            BigDecimal minRentalYield,
            String city, 
            String state) {

        String sql = """
            SELECT p.id, p.title, p.price, p.monthlyRental,
                   (p.monthlyRental * 12 / p.price * 100) as rentalYield,
                   p.city, p.state, p.propertyType
            FROM Property p
            WHERE p.monthlyRental IS NOT NULL
            AND p.price > 0
            AND (p.monthlyRental * 12 / p.price * 100) >= :minRentalYield
            AND (:city IS NULL OR p.city = :city)
            AND (:state IS NULL OR p.state = :state)
            AND p.status = com.realestate.model.entity.Property$PropertyStatus.AVAILABLE
            AND p.isDeleted = false
            ORDER BY (p.monthlyRental * 12 / p.price * 100) DESC
            """;

        Query query = entityManager.createQuery(sql);
        query.setParameter("minRentalYield", minRentalYield);
        query.setParameter("city", city);
        query.setParameter("state", state);

        List<Object[]> results = query.getResultList();
        
        return results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("title", row[1]);
            map.put("price", row[2]);
            map.put("monthlyRental", row[3]);
            map.put("rentalYield", row[4]);
            map.put("city", row[5]);
            map.put("state", row[6]);
            map.put("propertyType", row[7]);
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Property> findHighAppreciationPotentialProperties(String city, String state, int limit) {
        // This is a simplified implementation. In a real scenario, you would integrate with market data
        String jpql = """
            SELECT p FROM Property p 
            WHERE p.city = :city AND p.state = :state
            AND p.status = com.realestate.model.entity.Property$PropertyStatus.AVAILABLE
            AND p.yearBuilt >= :minYear
            AND p.isDeleted = false
            ORDER BY p.viewCount DESC, p.listingDate DESC
            """;

        TypedQuery<Property> query = entityManager.createQuery(jpql, Property.class);
        query.setParameter("city", city);
        query.setParameter("state", state);
        query.setParameter("minYear", 2000); // Properties built after 2000
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public Page<Property> searchPropertiesWithComplexFilters(Map<String, Object> filters, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> query = cb.createQuery(Property.class);
        Root<Property> property = query.from(Property.class);

        List<Predicate> predicates = buildPredicatesFromFilters(cb, property, filters);
        query.where(predicates.toArray(new Predicate[0]));

        // Count query for pagination
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Property> countProperty = countQuery.from(Property.class);
        List<Predicate> countPredicates = buildPredicatesFromFilters(cb, countProperty, filters);
        countQuery.select(cb.count(countProperty));
        countQuery.where(countPredicates.toArray(new Predicate[0]));

        // Apply sorting
        if (pageable.getSort().isSorted()) {
            List<Order> orders = pageable.getSort().stream()
                    .map(order -> order.isAscending() 
                            ? cb.asc(property.get(order.getProperty()))
                            : cb.desc(property.get(order.getProperty())))
                    .collect(Collectors.toList());
            query.orderBy(orders);
        }

        TypedQuery<Property> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        Long total = entityManager.createQuery(countQuery).getSingleResult();
        List<Property> properties = typedQuery.getResultList();

        return new PageImpl<>(properties, pageable, total);
    }

    @Override
    public List<Property> findRentalInvestmentProperties(
            BigDecimal maxPrice, 
            BigDecimal minRentalYield,
            String city, 
            String state, 
            int limit) {

        String jpql = """
            SELECT p FROM Property p 
            WHERE p.price <= :maxPrice
            AND p.monthlyRental IS NOT NULL
            AND (p.monthlyRental * 12 / p.price * 100) >= :minRentalYield
            AND (:city IS NULL OR p.city = :city)
            AND (:state IS NULL OR p.state = :state)
            AND p.status = com.realestate.model.entity.Property$PropertyStatus.AVAILABLE
            AND p.isDeleted = false
            ORDER BY (p.monthlyRental * 12 / p.price * 100) DESC
            """;

        TypedQuery<Property> query = entityManager.createQuery(jpql, Property.class);
        query.setParameter("maxPrice", maxPrice);
        query.setParameter("minRentalYield", minRentalYield);
        query.setParameter("city", city);
        query.setParameter("state", state);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<Map<String, Object>> compareProperties(List<Long> propertyIds) {
        if (propertyIds.isEmpty()) {
            return new ArrayList<>();
        }

        String jpql = """
            SELECT p.id, p.title, p.price, p.area, p.bedrooms, p.bathrooms,
                   p.city, p.state, p.propertyType, p.monthlyRental, p.viewCount
            FROM Property p 
            WHERE p.id IN :propertyIds
            AND p.isDeleted = false
            """;

        Query query = entityManager.createQuery(jpql);
        query.setParameter("propertyIds", propertyIds);

        List<Object[]> results = query.getResultList();
        
        return results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("title", row[1]);
            map.put("price", row[2]);
            map.put("area", row[3]);
            map.put("bedrooms", row[4]);
            map.put("bathrooms", row[5]);
            map.put("city", row[6]);
            map.put("state", row[7]);
            map.put("propertyType", row[8]);
            map.put("monthlyRental", row[9]);
            map.put("viewCount", row[10]);
            if (row[2] != null && row[3] != null && ((BigDecimal) row[3]).compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal pricePerSqft = ((BigDecimal) row[2]).divide((BigDecimal) row[3], 2, BigDecimal.ROUND_HALF_UP);
                map.put("pricePerSqft", pricePerSqft);
            }
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Property> findTrendingProperties(int days, int limit) {
        String jpql = """
            SELECT p FROM Property p 
            WHERE p.listingDate >= CURRENT_DATE - :days
            AND p.status = com.realestate.model.entity.Property$PropertyStatus.AVAILABLE
            AND p.isDeleted = false
            ORDER BY p.viewCount DESC, p.listingDate DESC
            """;

        TypedQuery<Property> query = entityManager.createQuery(jpql, Property.class);
        query.setParameter("days", days);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public Map<String, Object> getPropertyPriceTrends(String city, String state, Property.PropertyType propertyType) {
        // Simplified implementation - in real scenario, this would analyze historical data
        Map<String, Object> trends = new HashMap<>();
        
        String avgPriceSql = """
            SELECT AVG(p.price), AVG(p.price / p.area) FROM Property p 
            WHERE p.city = :city AND p.state = :state 
            AND (:propertyType IS NULL OR p.propertyType = :propertyType)
            AND p.isDeleted = false
            """;
        
        Object[] result = (Object[]) entityManager.createQuery(avgPriceSql)
                .setParameter("city", city)
                .setParameter("state", state)
                .setParameter("propertyType", propertyType)
                .getSingleResult();

        trends.put("averagePrice", result[0]);
        trends.put("averagePricePerSqft", result[1]);
        trends.put("city", city);
        trends.put("state", state);
        trends.put("propertyType", propertyType);

        return trends;
    }

    @Override
    public List<Map<String, Object>> findPropertiesWithScoring(
            Map<String, Object> preferences, 
            Map<String, Double> weights,
            int limit) {
        
        // This is a simplified scoring implementation
        // In a real scenario, you would implement a more sophisticated scoring algorithm
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> query = cb.createQuery(Property.class);
        Root<Property> property = query.from(Property.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(property.get("isDeleted"), false));
        predicates.add(cb.equal(property.get("status"), Property.PropertyStatus.AVAILABLE));

        // Apply basic filters from preferences
        if (preferences.containsKey("city")) {
            predicates.add(cb.equal(property.get("city"), preferences.get("city")));
        }
        if (preferences.containsKey("propertyType")) {
            predicates.add(cb.equal(property.get("propertyType"), preferences.get("propertyType")));
        }
        if (preferences.containsKey("maxPrice")) {
            predicates.add(cb.lessThanOrEqualTo(property.get("price"), (BigDecimal) preferences.get("maxPrice")));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(property.get("viewCount")), cb.desc(property.get("listingDate")));

        TypedQuery<Property> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(limit);
        
        List<Property> properties = typedQuery.getResultList();
        
        // Convert to scored results
        return properties.stream().map(p -> {
            Map<String, Object> result = new HashMap<>();
            result.put("property", p);
            result.put("score", calculatePropertyScore(p, preferences, weights));
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getPropertyMarketHeatMap(String state) {
        String sql = """
            SELECT p.city, COUNT(p) as propertyCount, AVG(p.price) as avgPrice,
                   AVG(p.price / p.area) as avgPricePerSqft
            FROM Property p 
            WHERE p.state = :state AND p.isDeleted = false
            GROUP BY p.city
            HAVING COUNT(p) > 0
            ORDER BY propertyCount DESC
            """;

        Query query = entityManager.createQuery(sql);
        query.setParameter("state", state);
        
        List<Object[]> results = query.getResultList();
        
        return results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("city", row[0]);
            map.put("propertyCount", row[1]);
            map.put("avgPrice", row[2]);
            map.put("avgPricePerSqft", row[3]);
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Property> findUndervaluedProperties(
            String city, 
            String state, 
            Property.PropertyType propertyType,
            Double discountPercentage, 
            int limit) {

        // Get market average price per sqft
        String avgPriceSql = """
            SELECT AVG(p.price / p.area) FROM Property p 
            WHERE p.city = :city AND p.state = :state 
            AND p.propertyType = :propertyType
            AND p.area > 0 AND p.isDeleted = false
            """;
        
        BigDecimal marketAvgPrice = entityManager.createQuery(avgPriceSql, BigDecimal.class)
                .setParameter("city", city)
                .setParameter("state", state)
                .setParameter("propertyType", propertyType)
                .getSingleResult();

        if (marketAvgPrice == null) {
            return new ArrayList<>();
        }

        BigDecimal discountThreshold = marketAvgPrice.multiply(BigDecimal.valueOf(1.0 - discountPercentage / 100.0));

        String jpql = """
            SELECT p FROM Property p 
            WHERE p.city = :city AND p.state = :state 
            AND p.propertyType = :propertyType
            AND p.area > 0
            AND (p.price / p.area) <= :discountThreshold
            AND p.status = com.realestate.model.entity.Property$PropertyStatus.AVAILABLE
            AND p.isDeleted = false
            ORDER BY (p.price / p.area) ASC
            """;

        TypedQuery<Property> query = entityManager.createQuery(jpql, Property.class);
        query.setParameter("city", city);
        query.setParameter("state", state);
        query.setParameter("propertyType", propertyType);
        query.setParameter("discountThreshold", discountThreshold);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public Map<String, Object> getPropertyInventoryAnalysis(String city, String state) {
        Map<String, Object> analysis = new HashMap<>();

        // Total properties
        String totalSql = "SELECT COUNT(p) FROM Property p WHERE p.city = :city AND p.state = :state AND p.isDeleted = false";
        Long total = entityManager.createQuery(totalSql, Long.class)
                .setParameter("city", city)
                .setParameter("state", state)
                .getSingleResult();

        // Available properties
        String availableSql = """
            SELECT COUNT(p) FROM Property p 
            WHERE p.city = :city AND p.state = :state 
            AND p.status = com.realestate.model.entity.Property$PropertyStatus.AVAILABLE
            AND p.isDeleted = false
            """;
        Long available = entityManager.createQuery(availableSql, Long.class)
                .setParameter("city", city)
                .setParameter("state", state)
                .getSingleResult();

        // Price distribution
        String priceDistSql = """
            SELECT p.propertyType, COUNT(p) as count, AVG(p.price) as avgPrice
            FROM Property p 
            WHERE p.city = :city AND p.state = :state AND p.isDeleted = false
            GROUP BY p.propertyType
            """;
        List<Object[]> priceResults = entityManager.createQuery(priceDistSql, Object[].class)
                .setParameter("city", city)
                .setParameter("state", state)
                .getResultList();

        Map<String, Map<String, Object>> typeDistribution = priceResults.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> {
                            Map<String, Object> typeData = new HashMap<>();
                            typeData.put("count", row[1]);
                            typeData.put("avgPrice", row[2]);
                            return typeData;
                        }
                ));

        analysis.put("totalProperties", total);
        analysis.put("availableProperties", available);
        analysis.put("typeDistribution", typeDistribution);
        analysis.put("city", city);
        analysis.put("state", state);

        return analysis;
    }

    // Helper methods
    private List<Predicate> buildPredicatesFromFilters(CriteriaBuilder cb, Root<Property> property, Map<String, Object> filters) {
        List<Predicate> predicates = new ArrayList<>();

        // Soft delete filter
        predicates.add(cb.equal(property.get("isDeleted"), false));

        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            String key = filter.getKey();
            Object value = filter.getValue();

            if (value == null) continue;

            switch (key) {
                case "propertyType":
                    predicates.add(cb.equal(property.get("propertyType"), value));
                    break;
                case "status":
                    predicates.add(cb.equal(property.get("status"), value));
                    break;
                case "city":
                    predicates.add(cb.like(cb.lower(property.get("city")), 
                                 cb.lower(cb.literal("%" + value + "%"))));
                    break;
                case "minPrice":
                    predicates.add(cb.greaterThanOrEqualTo(property.get("price"), (BigDecimal) value));
                    break;
                case "maxPrice":
                    predicates.add(cb.lessThanOrEqualTo(property.get("price"), (BigDecimal) value));
                    break;
                // Add more filters as needed
            }
        }

        return predicates;
    }

    private Double calculatePropertyScore(Property property, Map<String, Object> preferences, Map<String, Double> weights) {
        double score = 0.0;
        
        // Simple scoring algorithm - can be made more sophisticated
        if (weights.containsKey("price") && property.getPrice() != null) {
            // Lower price gets higher score (assuming budget is important)
            score += weights.get("price") * (1.0 / property.getPrice().doubleValue()) * 1000000;
        }
        
        if (weights.containsKey("area") && property.getArea() != null) {
            // Larger area gets higher score
            score += weights.get("area") * property.getArea().doubleValue() / 1000;
        }
        
        if (weights.containsKey("location") && property.getViewCount() != null) {
            // Popular locations (high view count) get higher score
            score += weights.get("location") * Math.log(property.getViewCount() + 1);
        }

        return score;
    }
}