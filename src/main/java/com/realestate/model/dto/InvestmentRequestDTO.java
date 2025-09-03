package com.realestate.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentRequestDTO {

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotNull(message = "Calculation type is required")
    private CalculationType calculationType;

    // Investment parameters
    @NotNull(message = "Initial investment is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Initial investment must be greater than 0")
    private BigDecimal initialInvestment;

    // Loan parameters
    @DecimalMin(value = "0.0", message = "Loan amount cannot be negative")
    private BigDecimal loanAmount;

    @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
    @DecimalMax(value = "50.0", message = "Interest rate cannot exceed 50%")
    private BigDecimal interestRate;

    @Min(value = 1, message = "Loan term must be at least 1 year")
    @Max(value = 50, message = "Loan term cannot exceed 50 years")
    private Integer loanTermYears;

    @DecimalMin(value = "0.0", message = "Down payment cannot be negative")
    private BigDecimal downPayment;

    // Rental parameters
    @DecimalMin(value = "0.0", message = "Monthly rental income cannot be negative")
    private BigDecimal monthlyRentalIncome;

    @DecimalMin(value = "0.0", message = "Monthly expenses cannot be negative")
    private BigDecimal monthlyExpenses;

    // Market parameters
    @DecimalMin(value = "-20.0", message = "Annual appreciation rate cannot be less than -20%")
    @DecimalMax(value = "50.0", message = "Annual appreciation rate cannot exceed 50%")
    private BigDecimal annualAppreciationRate;

    // Property expenses
    private BigDecimal annualMaintenance;
    private BigDecimal propertyTax;
    private BigDecimal hoaFees;
    private BigDecimal insuranceCost;

    // Analysis parameters
    @Min(value = 1, message = "Analysis years must be at least 1")
    @Max(value = 50, message = "Analysis years cannot exceed 50")
    @Builder.Default
    private Integer analysisYears = 10;

    @DecimalMin(value = "0.0", message = "Vacancy rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Vacancy rate cannot exceed 100%")
    @Builder.Default
    private BigDecimal vacancyRate = BigDecimal.valueOf(5.0);

    @DecimalMin(value = "0.0", message = "Management fee cannot be negative")
    @DecimalMax(value = "50.0", message = "Management fee cannot exceed 50%")
    @Builder.Default
    private BigDecimal managementFeePercentage = BigDecimal.valueOf(8.0);

    // Additional parameters for complex calculations
    private Map<String, String> additionalParameters;

    private String notes;
    @Builder.Default
    private Boolean saveCalculation = false;

    public enum CalculationType {
        ROI, MORTGAGE, RENTAL_YIELD, APPRECIATION, CASH_FLOW, COMPLETE_ANALYSIS
    }
    
    // Helper methods to map to names used in InvestmentCalculatorService
    public BigDecimal getMonthlyRental() {
        return monthlyRentalIncome;
    }
    
    public BigDecimal getAnnualExpenses() {
        return annualMaintenance != null ? annualMaintenance : BigDecimal.ZERO;
    }
    
    public BigDecimal getMonthlyMortgage() {
        return BigDecimal.ZERO; // This would need to be calculated
    }
    
    public BigDecimal getAppreciationRate() {
        return annualAppreciationRate;
    }
    
    public Integer getYears() {
        return analysisYears;
    }
}