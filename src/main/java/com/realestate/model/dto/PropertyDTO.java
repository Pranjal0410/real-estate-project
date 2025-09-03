package com.realestate.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Location is required")
    @Size(max = 500)
    private String location;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Pattern(regexp = "^\\d{6}$", message = "Pincode must be 6 digits")
    private String pincode;

    @NotNull(message = "Property type is required")
    private PropertyType propertyType;

    @NotNull(message = "Area is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Area must be greater than 0")
    private BigDecimal area;

    private AreaUnit areaUnit;

    @Min(value = 0, message = "Bedrooms cannot be negative")
    @Max(value = 20, message = "Bedrooms cannot exceed 20")
    private Integer bedrooms;

    @Min(value = 0, message = "Bathrooms cannot be negative")
    @Max(value = 10, message = "Bathrooms cannot exceed 10")
    private Integer bathrooms;

    @Min(value = 0, message = "Parking spaces cannot be negative")
    private Integer parkingSpaces;

    @Min(value = 1800, message = "Year built cannot be before 1800")
    @Max(value = 2100, message = "Year built cannot be in far future")
    private Integer yearBuilt;

    private String description;

    private PropertyStatus status;

    private Boolean isFurnished;
    private Boolean hasGarden;
    private Boolean hasPool;
    private Boolean hasGarage;

    private Set<String> amenities;
    private Set<String> imageUrls;

    private BigDecimal monthlyRental;
    private BigDecimal annualMaintenance;
    private BigDecimal propertyTax;
    private BigDecimal hoaFees;
    private BigDecimal insuranceCost;

    private LocalDateTime listingDate;
    private LocalDateTime soldDate;
    private Long viewCount;

    private Double latitude;
    private Double longitude;

    // Owner information (limited for security)
    private Long ownerId;
    private String ownerName;
    private String ownerPhone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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