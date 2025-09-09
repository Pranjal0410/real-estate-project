package com.realestate.controller;

import com.realestate.model.dto.ApiResponse;
import com.realestate.model.dto.InvestmentCalculationDTO;
import com.realestate.model.dto.InvestmentRequestDTO;
import com.realestate.service.InvestmentCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/investments")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
public class InvestmentController {

    private final InvestmentCalculatorService investmentCalculatorService;

    @PostMapping("/calculate")
    public CompletableFuture<ResponseEntity<ApiResponse<InvestmentCalculationDTO>>> calculateInvestment(
            @Valid @RequestBody InvestmentRequestDTO request) {
        log.info("Investment calculation request for property: {}", request.getPropertyId());
        
        return investmentCalculatorService.calculateInvestmentMetrics(request)
            .thenApply(result -> ResponseEntity.ok(
                ApiResponse.success(result, "Investment metrics calculated")));
    }

    @PostMapping("/analyze/{propertyId}")
    public CompletableFuture<ResponseEntity<ApiResponse<Map<String, BigDecimal>>>> analyzeInvestment(
            @PathVariable Long propertyId,
            @Valid @RequestBody InvestmentRequestDTO request) {
        log.info("Complex analysis request for property: {}", propertyId);
        
        return investmentCalculatorService.performComplexAnalysis(propertyId, request)
            .thenApply(result -> ResponseEntity.ok(
                ApiResponse.success(result, "Complex analysis completed")));
    }

    @GetMapping("/roi")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateROI(
            @RequestParam BigDecimal propertyPrice,
            @RequestParam BigDecimal monthlyRental,
            @RequestParam(required = false) BigDecimal annualExpenses) {
        
        BigDecimal roi = investmentCalculatorService.calculateROI(
            propertyPrice, monthlyRental, annualExpenses);
        
        return ResponseEntity.ok(ApiResponse.success(roi, "ROI calculated"));
    }

    @GetMapping("/rental-yield")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateRentalYield(
            @RequestParam BigDecimal propertyPrice,
            @RequestParam BigDecimal monthlyRental) {
        
        BigDecimal yield = investmentCalculatorService.calculateRentalYield(
            propertyPrice, monthlyRental);
        
        return ResponseEntity.ok(ApiResponse.success(yield, "Rental yield calculated"));
    }

    @GetMapping("/cash-flow")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateCashFlow(
            @RequestParam BigDecimal monthlyRental,
            @RequestParam(required = false) BigDecimal monthlyMortgage,
            @RequestParam(required = false) BigDecimal monthlyExpenses) {
        
        BigDecimal cashFlow = investmentCalculatorService.calculateMonthlyCashFlow(
            monthlyRental, monthlyMortgage, monthlyExpenses);
        
        return ResponseEntity.ok(ApiResponse.success(cashFlow, "Cash flow calculated"));
    }

    @GetMapping("/cap-rate")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateCapRate(
            @RequestParam BigDecimal propertyPrice,
            @RequestParam BigDecimal monthlyRental,
            @RequestParam(required = false) BigDecimal annualExpenses) {
        
        BigDecimal capRate = investmentCalculatorService.calculateCapRate(
            propertyPrice, monthlyRental, annualExpenses);
        
        return ResponseEntity.ok(ApiResponse.success(capRate, "Cap rate calculated"));
    }

    @GetMapping("/appreciation")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateAppreciation(
            @RequestParam BigDecimal initialValue,
            @RequestParam BigDecimal appreciationRate,
            @RequestParam Integer years) {
        
        BigDecimal appreciation = investmentCalculatorService.calculateAppreciation(
            initialValue, appreciationRate, years);
        
        return ResponseEntity.ok(ApiResponse.success(appreciation, "Appreciation calculated"));
    }

    @GetMapping("/mortgage")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateMortgage(
            @RequestParam BigDecimal loanAmount,
            @RequestParam BigDecimal annualRate,
            @RequestParam Integer months) {
        
        BigDecimal payment = investmentCalculatorService.calculateMortgagePayment(
            loanAmount, annualRate, months);
        
        return ResponseEntity.ok(ApiResponse.success(payment, "Mortgage payment calculated"));
    }

    @GetMapping("/break-even")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateBreakEven(
            @RequestParam BigDecimal propertyPrice,
            @RequestParam BigDecimal monthlyRental,
            @RequestParam(required = false) BigDecimal annualExpenses) {
        
        BigDecimal breakEven = investmentCalculatorService.calculateBreakEvenPoint(
            propertyPrice, monthlyRental, annualExpenses);
        
        return ResponseEntity.ok(ApiResponse.success(breakEven, "Break-even point calculated"));
    }

    @GetMapping("/total-return")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateTotalReturn(
            @RequestParam BigDecimal initialInvestment,
            @RequestParam BigDecimal monthlyRental,
            @RequestParam BigDecimal appreciationRate,
            @RequestParam Integer years) {
        
        BigDecimal totalReturn = investmentCalculatorService.calculateTotalReturn(
            initialInvestment, monthlyRental, appreciationRate, years);
        
        return ResponseEntity.ok(ApiResponse.success(totalReturn, "Total return calculated"));
    }
}