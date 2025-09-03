package com.realestate.service;

import com.realestate.exception.BusinessException;
import com.realestate.model.dto.InvestmentCalculationDTO;
import com.realestate.model.dto.InvestmentRequestDTO;
import com.realestate.model.entity.InvestmentCalculation;
import com.realestate.model.entity.Property;
import com.realestate.repository.InvestmentCalculationRepository;
import com.realestate.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InvestmentCalculatorService {

    private final PropertyRepository propertyRepository;
    private final InvestmentCalculationRepository investmentCalculationRepository;
    
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    
    private static final BigDecimal MONTHS_IN_YEAR = new BigDecimal("12");
    private static final BigDecimal PERCENT = new BigDecimal("100");
    
    @Async
    public CompletableFuture<InvestmentCalculationDTO> calculateInvestmentMetrics(InvestmentRequestDTO request) {
        log.info("Starting investment calculation for property: {}", request.getPropertyId());
        
        return CompletableFuture.supplyAsync(() -> {
            Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new BusinessException("Property not found"));
                
            InvestmentCalculationDTO dto = new InvestmentCalculationDTO();
            dto.setPropertyId(property.getId());
            dto.setPropertyPrice(property.getPrice());
            
            BigDecimal roi = calculateROI(property.getPrice(), request.getMonthlyRental(), 
                request.getAnnualExpenses());
            dto.setRoi(roi);
            
            BigDecimal rentalYield = calculateRentalYield(property.getPrice(), 
                request.getMonthlyRental());
            dto.setRentalYield(rentalYield);
            
            BigDecimal cashFlow = calculateMonthlyCashFlow(request.getMonthlyRental(), 
                request.getMonthlyMortgage(), request.getMonthlyExpenses());
            dto.setCashFlow(cashFlow);
            
            BigDecimal capRate = calculateCapRate(property.getPrice(), 
                request.getMonthlyRental(), request.getAnnualExpenses());
            dto.setCapRate(capRate);
            
            BigDecimal appreciationValue = calculateAppreciation(property.getPrice(), 
                request.getAppreciationRate(), request.getYears());
            dto.setAppreciationValue(appreciationValue);
            
            BigDecimal breakEvenPoint = calculateBreakEvenPoint(property.getPrice(), 
                request.getMonthlyRental(), request.getAnnualExpenses());
            dto.setBreakEvenPoint(breakEvenPoint);
            
            saveCalculation(property, dto);
            
            log.info("Investment calculation completed for property: {}", property.getId());
            return dto;
        }, executorService);
    }
    
    public BigDecimal calculateROI(BigDecimal propertyPrice, BigDecimal monthlyRental, 
                                   BigDecimal annualExpenses) {
        validatePositive(propertyPrice, "Property price");
        validatePositive(monthlyRental, "Monthly rental");
        
        BigDecimal annualRental = monthlyRental.multiply(MONTHS_IN_YEAR);
        BigDecimal netIncome = annualRental.subtract(Optional.ofNullable(annualExpenses)
            .orElse(BigDecimal.ZERO));
        
        return netIncome.divide(propertyPrice, 4, RoundingMode.HALF_UP)
            .multiply(PERCENT);
    }
    
    public BigDecimal calculateRentalYield(BigDecimal propertyPrice, BigDecimal monthlyRental) {
        validatePositive(propertyPrice, "Property price");
        validatePositive(monthlyRental, "Monthly rental");
        
        BigDecimal annualRental = monthlyRental.multiply(MONTHS_IN_YEAR);
        
        return annualRental.divide(propertyPrice, 4, RoundingMode.HALF_UP)
            .multiply(PERCENT);
    }
    
    public BigDecimal calculateMonthlyCashFlow(BigDecimal monthlyRental, 
                                               BigDecimal monthlyMortgage, 
                                               BigDecimal monthlyExpenses) {
        BigDecimal rental = Optional.ofNullable(monthlyRental).orElse(BigDecimal.ZERO);
        BigDecimal mortgage = Optional.ofNullable(monthlyMortgage).orElse(BigDecimal.ZERO);
        BigDecimal expenses = Optional.ofNullable(monthlyExpenses).orElse(BigDecimal.ZERO);
        
        return rental.subtract(mortgage).subtract(expenses);
    }
    
    public BigDecimal calculateCapRate(BigDecimal propertyPrice, BigDecimal monthlyRental, 
                                       BigDecimal annualExpenses) {
        validatePositive(propertyPrice, "Property price");
        validatePositive(monthlyRental, "Monthly rental");
        
        BigDecimal annualRental = monthlyRental.multiply(MONTHS_IN_YEAR);
        BigDecimal netOperatingIncome = annualRental.subtract(
            Optional.ofNullable(annualExpenses).orElse(BigDecimal.ZERO));
        
        return netOperatingIncome.divide(propertyPrice, 4, RoundingMode.HALF_UP)
            .multiply(PERCENT);
    }
    
    public BigDecimal calculateAppreciation(BigDecimal initialValue, BigDecimal appreciationRate, 
                                           Integer years) {
        validatePositive(initialValue, "Initial value");
        
        if (appreciationRate == null || years == null || years <= 0) {
            return initialValue;
        }
        
        BigDecimal rate = BigDecimal.ONE.add(appreciationRate.divide(PERCENT, 
            4, RoundingMode.HALF_UP));
        BigDecimal multiplier = rate.pow(years);
        
        return initialValue.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal calculateMortgagePayment(BigDecimal loanAmount, BigDecimal annualRate, 
                                               Integer months) {
        validatePositive(loanAmount, "Loan amount");
        validatePositive(annualRate, "Annual rate");
        
        if (months == null || months <= 0) {
            throw new BusinessException("Invalid loan term");
        }
        
        BigDecimal monthlyRate = annualRate.divide(PERCENT, 10, RoundingMode.HALF_UP)
            .divide(MONTHS_IN_YEAR, 10, RoundingMode.HALF_UP);
        
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return loanAmount.divide(new BigDecimal(months), 2, RoundingMode.HALF_UP);
        }
        
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal power = onePlusR.pow(months);
        BigDecimal numerator = loanAmount.multiply(monthlyRate).multiply(power);
        BigDecimal denominator = power.subtract(BigDecimal.ONE);
        
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal calculateBreakEvenPoint(BigDecimal propertyPrice, BigDecimal monthlyRental, 
                                              BigDecimal annualExpenses) {
        validatePositive(propertyPrice, "Property price");
        validatePositive(monthlyRental, "Monthly rental");
        
        BigDecimal annualRental = monthlyRental.multiply(MONTHS_IN_YEAR);
        BigDecimal netAnnualIncome = annualRental.subtract(
            Optional.ofNullable(annualExpenses).orElse(BigDecimal.ZERO));
        
        if (netAnnualIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        return propertyPrice.divide(netAnnualIncome, 2, RoundingMode.HALF_UP);
    }
    
    @Async
    public CompletableFuture<Map<String, BigDecimal>> performComplexAnalysis(
            Long propertyId, InvestmentRequestDTO request) {
        
        return CompletableFuture.supplyAsync(() -> {
            Map<String, BigDecimal> analysis = new HashMap<>();
            
            Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new BusinessException("Property not found"));
            
            CompletableFuture<BigDecimal> roiFuture = CompletableFuture.supplyAsync(() ->
                calculateROI(property.getPrice(), request.getMonthlyRental(), 
                    request.getAnnualExpenses()));
                    
            CompletableFuture<BigDecimal> yieldFuture = CompletableFuture.supplyAsync(() ->
                calculateRentalYield(property.getPrice(), request.getMonthlyRental()));
                
            CompletableFuture<BigDecimal> capRateFuture = CompletableFuture.supplyAsync(() ->
                calculateCapRate(property.getPrice(), request.getMonthlyRental(), 
                    request.getAnnualExpenses()));
                    
            try {
                analysis.put("roi", roiFuture.get());
                analysis.put("rentalYield", yieldFuture.get());
                analysis.put("capRate", capRateFuture.get());
                
                BigDecimal totalReturn = calculateTotalReturn(property.getPrice(), 
                    request.getMonthlyRental(), request.getAppreciationRate(), 
                    request.getYears());
                analysis.put("totalReturn", totalReturn);
                
                BigDecimal irrEstimate = estimateIRR(property.getPrice(), 
                    request.getMonthlyRental(), request.getYears());
                analysis.put("irr", irrEstimate);
                
            } catch (Exception e) {
                log.error("Error in complex analysis", e);
                throw new BusinessException("Failed to complete analysis");
            }
            
            return analysis;
        }, executorService);
    }
    
    public BigDecimal calculateTotalReturn(BigDecimal initialInvestment, BigDecimal monthlyRental,
                                          BigDecimal appreciationRate, Integer years) {
        BigDecimal rentalIncome = monthlyRental.multiply(MONTHS_IN_YEAR)
            .multiply(new BigDecimal(years));
        BigDecimal appreciatedValue = calculateAppreciation(initialInvestment, 
            appreciationRate, years);
        BigDecimal totalValue = rentalIncome.add(appreciatedValue);
        
        return totalValue.subtract(initialInvestment)
            .divide(initialInvestment, 4, RoundingMode.HALF_UP)
            .multiply(PERCENT);
    }
    
    private BigDecimal estimateIRR(BigDecimal initialInvestment, BigDecimal monthlyRental, 
                                   Integer years) {
        BigDecimal annualCashFlow = monthlyRental.multiply(MONTHS_IN_YEAR);
        BigDecimal totalCashFlow = annualCashFlow.multiply(new BigDecimal(years));
        
        BigDecimal simpleReturn = totalCashFlow.divide(initialInvestment, 
            4, RoundingMode.HALF_UP);
        
        return simpleReturn.divide(new BigDecimal(years), 4, RoundingMode.HALF_UP)
            .multiply(PERCENT);
    }
    
    public Function<BigDecimal, BigDecimal> createTaxCalculator(BigDecimal taxRate) {
        return income -> income.multiply(taxRate.divide(PERCENT, 4, RoundingMode.HALF_UP));
    }
    
    public BiFunction<BigDecimal, BigDecimal, BigDecimal> createLeverageCalculator() {
        return (equity, debt) -> {
            BigDecimal total = equity.add(debt);
            if (equity.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
            return total.divide(equity, 2, RoundingMode.HALF_UP);
        };
    }
    
    private void saveCalculation(Property property, InvestmentCalculationDTO dto) {
        InvestmentCalculation calculation = new InvestmentCalculation();
        calculation.setProperty(property);
        calculation.setRoi(dto.getRoi());
        calculation.setRentalYield(dto.getRentalYield());
        calculation.setAppreciationRate(dto.getAppreciationValue());
        calculation.setCalculatedAt(new Date());
        
        investmentCalculationRepository.save(calculation);
    }
    
    private void validatePositive(BigDecimal value, String fieldName) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(fieldName + " must be positive");
        }
    }
    
    public Optional<BigDecimal> calculateOptionalMetric(Optional<BigDecimal> value1, 
                                                        Optional<BigDecimal> value2,
                                                        BiFunction<BigDecimal, BigDecimal, BigDecimal> operation) {
        return value1.flatMap(v1 -> 
            value2.map(v2 -> operation.apply(v1, v2))
        );
    }
}