package com.realestate.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvestmentCalculationDTO {

    private Long id;

    private Long propertyId;
    private String propertyTitle;
    private String propertyLocation;

    private Long userId;
    private String userName;

    @NotNull(message = "Calculation type is required")
    private CalculationType calculationType;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal initialInvestment;

    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private Integer loanTermYears;
    private BigDecimal downPayment;

    private BigDecimal monthlyRentalIncome;
    private BigDecimal monthlyExpenses;
    private BigDecimal annualAppreciationRate;

    // Additional fields needed by InvestmentCalculatorService
    private BigDecimal propertyPrice;
    private BigDecimal roi;
    private BigDecimal rentalYield;
    private BigDecimal appreciationValue;
    private BigDecimal breakEvenPoint;
    
    // Calculation Results
    private BigDecimal roiPercentage;
    private BigDecimal cashFlow;
    private BigDecimal capRate;
    private BigDecimal grossRentalYield;
    private BigDecimal netRentalYield;
    private BigDecimal cashOnCashReturn;
    private BigDecimal breakEvenYears;
    private BigDecimal totalReturn10Years;
    private BigDecimal monthlyMortgagePayment;
    private BigDecimal totalInterestPaid;

    private LocalDateTime calculationDate;
    private Map<String, String> additionalParameters;
    private String notes;
    private Boolean isSaved;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum CalculationType {
        ROI, MORTGAGE, RENTAL_YIELD, APPRECIATION, CASH_FLOW, COMPLETE_ANALYSIS
    }
}