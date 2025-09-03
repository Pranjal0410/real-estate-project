package com.realestate.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarketDataDTO {

    private Long id;

    @NotBlank(message = "Location is required")
    private String location;

    private String city;
    private String state;
    private String pincode;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal avgPricePerSqft;

    private BigDecimal medianPrice;
    private BigDecimal avgRentalPerSqft;

    private BigDecimal growthRateYoy;
    private BigDecimal growthRateQoq;
    private BigDecimal rentalGrowthRate;

    private BigDecimal vacancyRate;
    private BigDecimal absorptionRate;
    private BigDecimal inventoryMonths;

    private Integer totalListings;
    private Integer newListings;
    private Integer propertiesSold;
    private Integer avgDaysOnMarket;

    private BigDecimal priceToRentRatio;

    private MarketTrend marketTrend;
    private PropertyType propertyType;

    @NotNull(message = "Data date is required")
    private LocalDate dataDate;

    private String dataSource;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum MarketTrend {
        BULLISH, BEARISH, NEUTRAL, RECOVERING, DECLINING
    }

    public enum PropertyType {
        RESIDENTIAL, COMMERCIAL, INDUSTRIAL, LAND, APARTMENT, VILLA, OFFICE, RETAIL, WAREHOUSE
    }
}