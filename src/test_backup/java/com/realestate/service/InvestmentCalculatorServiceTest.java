package com.realestate.service;

import com.realestate.exception.BusinessException;
import com.realestate.model.dto.InvestmentCalculationDTO;
import com.realestate.model.dto.InvestmentRequestDTO;
import com.realestate.model.entity.Property;
import com.realestate.repository.InvestmentCalculationRepository;
import com.realestate.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentCalculatorServiceTest {

    @Mock
    private PropertyRepository propertyRepository;
    
    @Mock
    private InvestmentCalculationRepository investmentCalculationRepository;
    
    @InjectMocks
    private InvestmentCalculatorService investmentCalculatorService;
    
    private Property testProperty;
    private InvestmentRequestDTO testRequest;
    
    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.setId(1L);
        testProperty.setPrice(new BigDecimal("500000"));
        
        testRequest = new InvestmentRequestDTO();
        testRequest.setPropertyId(1L);
        testRequest.setMonthlyRental(new BigDecimal("3000"));
        testRequest.setAnnualExpenses(new BigDecimal("12000"));
        testRequest.setMonthlyMortgage(new BigDecimal("2000"));
        testRequest.setMonthlyExpenses(new BigDecimal("500"));
        testRequest.setAppreciationRate(new BigDecimal("3"));
        testRequest.setYears(5);
    }
    
    @Test
    void testCalculateROI_Success() {
        BigDecimal propertyPrice = new BigDecimal("500000");
        BigDecimal monthlyRental = new BigDecimal("3000");
        BigDecimal annualExpenses = new BigDecimal("12000");
        
        BigDecimal roi = investmentCalculatorService.calculateROI(
            propertyPrice, monthlyRental, annualExpenses);
        
        assertNotNull(roi);
        assertEquals(new BigDecimal("4.8000"), roi);
    }
    
    @Test
    void testCalculateROI_InvalidPrice() {
        assertThrows(BusinessException.class, () -> {
            investmentCalculatorService.calculateROI(
                BigDecimal.ZERO, 
                new BigDecimal("3000"), 
                new BigDecimal("12000"));
        });
    }
    
    @Test
    void testCalculateRentalYield() {
        BigDecimal propertyPrice = new BigDecimal("500000");
        BigDecimal monthlyRental = new BigDecimal("3000");
        
        BigDecimal yield = investmentCalculatorService.calculateRentalYield(
            propertyPrice, monthlyRental);
        
        assertNotNull(yield);
        assertEquals(new BigDecimal("7.2000"), yield);
    }
    
    @Test
    void testCalculateMonthlyCashFlow() {
        BigDecimal monthlyRental = new BigDecimal("3000");
        BigDecimal monthlyMortgage = new BigDecimal("2000");
        BigDecimal monthlyExpenses = new BigDecimal("500");
        
        BigDecimal cashFlow = investmentCalculatorService.calculateMonthlyCashFlow(
            monthlyRental, monthlyMortgage, monthlyExpenses);
        
        assertNotNull(cashFlow);
        assertEquals(new BigDecimal("500"), cashFlow);
    }
    
    @Test
    void testCalculateCapRate() {
        BigDecimal propertyPrice = new BigDecimal("500000");
        BigDecimal monthlyRental = new BigDecimal("3000");
        BigDecimal annualExpenses = new BigDecimal("12000");
        
        BigDecimal capRate = investmentCalculatorService.calculateCapRate(
            propertyPrice, monthlyRental, annualExpenses);
        
        assertNotNull(capRate);
        assertEquals(new BigDecimal("4.8000"), capRate);
    }
    
    @Test
    void testCalculateAppreciation() {
        BigDecimal initialValue = new BigDecimal("500000");
        BigDecimal appreciationRate = new BigDecimal("3");
        Integer years = 5;
        
        BigDecimal appreciation = investmentCalculatorService.calculateAppreciation(
            initialValue, appreciationRate, years);
        
        assertNotNull(appreciation);
        assertTrue(appreciation.compareTo(initialValue) > 0);
    }
    
    @Test
    void testCalculateMortgagePayment() {
        BigDecimal loanAmount = new BigDecimal("400000");
        BigDecimal annualRate = new BigDecimal("4.5");
        Integer months = 360;
        
        BigDecimal payment = investmentCalculatorService.calculateMortgagePayment(
            loanAmount, annualRate, months);
        
        assertNotNull(payment);
        assertTrue(payment.compareTo(BigDecimal.ZERO) > 0);
    }
    
    @Test
    void testCalculateMortgagePayment_InvalidLoanTerm() {
        assertThrows(BusinessException.class, () -> {
            investmentCalculatorService.calculateMortgagePayment(
                new BigDecimal("400000"), 
                new BigDecimal("4.5"), 
                0);
        });
    }
    
    @Test
    void testCalculateBreakEvenPoint() {
        BigDecimal propertyPrice = new BigDecimal("500000");
        BigDecimal monthlyRental = new BigDecimal("3000");
        BigDecimal annualExpenses = new BigDecimal("12000");
        
        BigDecimal breakEven = investmentCalculatorService.calculateBreakEvenPoint(
            propertyPrice, monthlyRental, annualExpenses);
        
        assertNotNull(breakEven);
        assertTrue(breakEven.compareTo(BigDecimal.ZERO) > 0);
    }
    
    @Test
    void testCalculateTotalReturn() {
        BigDecimal initialInvestment = new BigDecimal("500000");
        BigDecimal monthlyRental = new BigDecimal("3000");
        BigDecimal appreciationRate = new BigDecimal("3");
        Integer years = 5;
        
        BigDecimal totalReturn = investmentCalculatorService.calculateTotalReturn(
            initialInvestment, monthlyRental, appreciationRate, years);
        
        assertNotNull(totalReturn);
        assertTrue(totalReturn.compareTo(BigDecimal.ZERO) > 0);
    }
    
    @Test
    void testCalculateInvestmentMetrics_Async() throws Exception {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(investmentCalculationRepository.save(any())).thenReturn(null);
        
        CompletableFuture<InvestmentCalculationDTO> future = 
            investmentCalculatorService.calculateInvestmentMetrics(testRequest);
        
        assertNotNull(future);
        InvestmentCalculationDTO result = future.get();
        assertNotNull(result);
        assertEquals(testProperty.getId(), result.getPropertyId());
    }
    
    @Test
    void testCalculateInvestmentMetrics_PropertyNotFound() {
        when(propertyRepository.findById(99L)).thenReturn(Optional.empty());
        
        InvestmentRequestDTO invalidRequest = new InvestmentRequestDTO();
        invalidRequest.setPropertyId(99L);
        
        CompletableFuture<InvestmentCalculationDTO> future = 
            investmentCalculatorService.calculateInvestmentMetrics(invalidRequest);
        
        assertThrows(Exception.class, future::get);
    }
    
    @Test
    void testCreateTaxCalculator() {
        BigDecimal taxRate = new BigDecimal("25");
        var taxCalculator = investmentCalculatorService.createTaxCalculator(taxRate);
        
        BigDecimal income = new BigDecimal("100000");
        BigDecimal tax = taxCalculator.apply(income);
        
        assertNotNull(tax);
        assertEquals(new BigDecimal("25000.00"), tax);
    }
    
    @Test
    void testCreateLeverageCalculator() {
        var leverageCalculator = investmentCalculatorService.createLeverageCalculator();
        
        BigDecimal equity = new BigDecimal("100000");
        BigDecimal debt = new BigDecimal("400000");
        BigDecimal leverage = leverageCalculator.apply(equity, debt);
        
        assertNotNull(leverage);
        assertEquals(new BigDecimal("5.00"), leverage);
    }
}