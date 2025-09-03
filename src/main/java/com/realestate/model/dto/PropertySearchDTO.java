package com.realestate.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertySearchDTO {

    // Location filters
    private String location;
    private String city;
    private String state;
    private String pincode;
    private List<String> locations;

    // Price filters
    @DecimalMin(value = "0.0", message = "Minimum price must be positive")
    private BigDecimal minPrice;
    
    @DecimalMin(value = "0.0", message = "Maximum price must be positive")
    private BigDecimal maxPrice;

    // Property type filters
    private PropertyType propertyType;
    private Set<PropertyType> propertyTypes;

    // Area filters
    @DecimalMin(value = "0.0", message = "Minimum area must be positive")
    private BigDecimal minArea;
    
    @DecimalMin(value = "0.0", message = "Maximum area must be positive")
    private BigDecimal maxArea;
    
    private AreaUnit areaUnit;

    // Room filters
    @Min(value = 0, message = "Minimum bedrooms cannot be negative")
    private Integer minBedrooms;
    
    @Min(value = 0, message = "Maximum bedrooms cannot be negative")
    private Integer maxBedrooms;

    @Min(value = 0, message = "Minimum bathrooms cannot be negative")
    private Integer minBathrooms;
    
    @Min(value = 0, message = "Maximum bathrooms cannot be negative")
    private Integer maxBathrooms;

    // Feature filters
    private Boolean isFurnished;
    private Boolean hasGarden;
    private Boolean hasPool;
    private Boolean hasGarage;
    private Boolean hasParkingSpace;

    // Amenities filter
    private Set<String> amenities;

    // Status filter
    private PropertyStatus status;
    private Set<PropertyStatus> statuses;

    // Year built filter
    private Integer minYearBuilt;
    private Integer maxYearBuilt;

    // Rental filters
    private BigDecimal minMonthlyRental;
    private BigDecimal maxMonthlyRental;

    // Location-based search
    private Double latitude;
    private Double longitude;
    private Double radiusKm; // Search radius in kilometers

    // Text search
    private String keyword;
    private String description;

    // Owner filters
    private Long ownerId;
    private String ownerName;

    // Sorting and pagination
    @Builder.Default
    private String sortBy = "createdAt"; // price, area, createdAt, updatedAt, viewCount
    @Builder.Default
    private String sortDirection = "DESC"; // ASC or DESC
    
    @Min(value = 0, message = "Page number cannot be negative")
    @Builder.Default
    private Integer page = 0;
    
    @Min(value = 1, message = "Page size must be at least 1")
    @Builder.Default
    private Integer size = 20;

    // Date filters
    private String listedAfter; // ISO date string
    private String listedBefore; // ISO date string

    public enum PropertyType {
        RESIDENTIAL, COMMERCIAL, INDUSTRIAL, LAND, APARTMENT, VILLA, OFFICE, RETAIL, WAREHOUSE
    }

    public enum PropertyStatus {
        AVAILABLE, SOLD, RENTED, UNDER_OFFER, OFF_MARKET
    }

    public enum AreaUnit {
        SQFT, SQMT, ACRE, HECTARE
    }
}