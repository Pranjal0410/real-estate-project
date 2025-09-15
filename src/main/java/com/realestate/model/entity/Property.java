package com.realestate.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "properties", 
       indexes = {
           @Index(name = "idx_property_location", columnList = "location"),
           @Index(name = "idx_property_type", columnList = "property_type"),
           @Index(name = "idx_property_price", columnList = "price"),
           @Index(name = "idx_property_status", columnList = "status")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Property extends BaseEntity {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    @Column(nullable = false)
    private String title;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @NotBlank(message = "Location is required")
    @Size(max = 500)
    @Column(nullable = false)
    private String location;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Pattern(regexp = "^\\d{6}$", message = "Pincode must be 6 digits")
    private String pincode;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    private PropertyType propertyType;

    @NotNull(message = "Area is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Area must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal area;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_unit")
    @Builder.Default
    private AreaUnit areaUnit = AreaUnit.SQFT;

    @Min(value = 0, message = "Bedrooms cannot be negative")
    @Max(value = 20, message = "Bedrooms cannot exceed 20")
    private Integer bedrooms;

    @Min(value = 0, message = "Bathrooms cannot be negative")
    @Max(value = 10, message = "Bathrooms cannot exceed 10")
    private Integer bathrooms;

    @Min(value = 0, message = "Parking spaces cannot be negative")
    @Column(name = "parking_spaces")
    private Integer parkingSpaces;

    @Min(value = 1800, message = "Year built cannot be before 1800")
    @Max(value = 2100, message = "Year built cannot be in far future")
    @Column(name = "year_built")
    private Integer yearBuilt;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PropertyStatus status = PropertyStatus.AVAILABLE;

    @Column(name = "is_furnished")
    @Builder.Default
    private Boolean isFurnished = false;

    @Column(name = "has_garden")
    @Builder.Default
    private Boolean hasGarden = false;

    @Column(name = "has_pool")
    @Builder.Default
    private Boolean hasPool = false;

    @Column(name = "has_garage")
    @Builder.Default
    private Boolean hasGarage = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_amenities", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "amenity")
    @Builder.Default
    private Set<String> amenities = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_images", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "image_url")
    @Builder.Default
    private Set<String> imageUrls = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<InvestmentCalculation> calculations = new HashSet<>();

    @Column(name = "monthly_rental")
    private BigDecimal monthlyRental;

    @Column(name = "annual_maintenance")
    private BigDecimal annualMaintenance;

    @Column(name = "property_tax")
    private BigDecimal propertyTax;

    @Column(name = "hoa_fees")
    private BigDecimal hoaFees;

    @Column(name = "insurance_cost")
    private BigDecimal insuranceCost;

    @Column(name = "listing_date")
    private LocalDateTime listingDate;

    @Column(name = "sold_date")
    private LocalDateTime soldDate;

    @Column(name = "view_count")
    @Builder.Default
    private Long viewCount = 0L;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @PrePersist
    protected void onCreate() {
        if (listingDate == null) {
            listingDate = LocalDateTime.now();
        }
    }

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