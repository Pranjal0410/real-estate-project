package com.realestate.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "investment_calculations",
       indexes = {
           @Index(name = "idx_calc_property", columnList = "property_id"),
           @Index(name = "idx_calc_user", columnList = "user_id"),
           @Index(name = "idx_calc_type", columnList = "calculation_type")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class InvestmentCalculation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_type", nullable = false)
    private CalculationType calculationType;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "initial_investment", precision = 15, scale = 2)
    private BigDecimal initialInvestment;

    @Column(name = "loan_amount", precision = 15, scale = 2)
    private BigDecimal loanAmount;

    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "loan_term_years")
    private Integer loanTermYears;

    @Column(name = "down_payment", precision = 15, scale = 2)
    private BigDecimal downPayment;

    @Column(name = "monthly_rental_income", precision = 10, scale = 2)
    private BigDecimal monthlyRentalIncome;

    @Column(name = "monthly_expenses", precision = 10, scale = 2)
    private BigDecimal monthlyExpenses;

    @Column(name = "annual_appreciation_rate", precision = 5, scale = 2)
    private BigDecimal annualAppreciationRate;
    
    // Additional fields needed by InvestmentCalculatorService
    @Column(name = "roi", precision = 5, scale = 2)
    private BigDecimal roi;
    
    @Column(name = "rental_yield", precision = 5, scale = 2)
    private BigDecimal rentalYield;
    
    @Column(name = "appreciation_rate", precision = 5, scale = 2)
    private BigDecimal appreciationRate;
    
    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;

    @Column(name = "roi_percentage", precision = 5, scale = 2)
    private BigDecimal roiPercentage;

    @Column(name = "cash_flow", precision = 10, scale = 2)
    private BigDecimal cashFlow;

    @Column(name = "cap_rate", precision = 5, scale = 2)
    private BigDecimal capRate;

    @Column(name = "gross_rental_yield", precision = 5, scale = 2)
    private BigDecimal grossRentalYield;

    @Column(name = "net_rental_yield", precision = 5, scale = 2)
    private BigDecimal netRentalYield;

    @Column(name = "cash_on_cash_return", precision = 5, scale = 2)
    private BigDecimal cashOnCashReturn;

    @Column(name = "break_even_years", precision = 5, scale = 2)
    private BigDecimal breakEvenYears;

    @Column(name = "total_return_10years", precision = 15, scale = 2)
    private BigDecimal totalReturn10Years;

    @Column(name = "monthly_mortgage_payment", precision = 10, scale = 2)
    private BigDecimal monthlyMortgagePayment;

    @Column(name = "total_interest_paid", precision = 15, scale = 2)
    private BigDecimal totalInterestPaid;

    @Column(name = "calculation_date")
    private LocalDateTime calculationDate;

    @ElementCollection
    @CollectionTable(name = "calculation_parameters", joinColumns = @JoinColumn(name = "calculation_id"))
    @MapKeyColumn(name = "parameter_key")
    @Column(name = "parameter_value")
    @Builder.Default
    private Map<String, String> additionalParameters = new HashMap<>();

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_saved")
    @Builder.Default
    private Boolean isSaved = false;

    @PrePersist
    protected void onCreate() {
        if (calculationDate == null) {
            calculationDate = LocalDateTime.now();
        }
    }

    public enum CalculationType {
        ROI, MORTGAGE, RENTAL_YIELD, APPRECIATION, CASH_FLOW, COMPLETE_ANALYSIS
    }
    
    // Helper method to set calculated date from Date type
    public void setCalculatedAt(Date date) {
        if (date != null) {
            this.calculatedAt = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        }
    }
}