package com.realestate.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "market_data",
       indexes = {
           @Index(name = "idx_market_location", columnList = "location"),
           @Index(name = "idx_market_date", columnList = "data_date"),
           @Index(name = "idx_market_location_date", columnList = "location, data_date")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class MarketData extends BaseEntity {

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "avg_price_per_sqft", nullable = false, precision = 10, scale = 2)
    private BigDecimal avgPricePerSqft;

    @Column(name = "median_price", precision = 15, scale = 2)
    private BigDecimal medianPrice;

    @Column(name = "avg_rental_per_sqft", precision = 10, scale = 2)
    private BigDecimal avgRentalPerSqft;

    @Column(name = "growth_rate_yoy", precision = 5, scale = 2)
    private BigDecimal growthRateYoy;

    @Column(name = "growth_rate_qoq", precision = 5, scale = 2)
    private BigDecimal growthRateQoq;

    @Column(name = "rental_growth_rate", precision = 5, scale = 2)
    private BigDecimal rentalGrowthRate;

    @Column(name = "vacancy_rate", precision = 5, scale = 2)
    private BigDecimal vacancyRate;

    @Column(name = "absorption_rate", precision = 5, scale = 2)
    private BigDecimal absorptionRate;

    @Column(name = "inventory_months")
    private BigDecimal inventoryMonths;

    @Column(name = "total_listings")
    private Integer totalListings;

    @Column(name = "new_listings")
    private Integer newListings;

    @Column(name = "properties_sold")
    private Integer propertiesSold;

    @Column(name = "avg_days_on_market")
    private Integer avgDaysOnMarket;

    @Column(name = "price_to_rent_ratio", precision = 5, scale = 2)
    private BigDecimal priceToRentRatio;

    @Enumerated(EnumType.STRING)
    @Column(name = "market_trend")
    private MarketTrend marketTrend;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type")
    private Property.PropertyType propertyType;

    @Column(name = "data_date", nullable = false)
    private LocalDate dataDate;

    @Column(name = "data_source")
    private String dataSource;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        if (dataDate == null) {
            dataDate = LocalDate.now();
        }
    }

    public enum MarketTrend {
        BULLISH, BEARISH, NEUTRAL, RECOVERING, DECLINING
    }
}