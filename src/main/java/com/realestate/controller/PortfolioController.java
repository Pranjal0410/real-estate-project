package com.realestate.controller;

import com.realestate.model.dto.*;
import com.realestate.model.entity.Holding;
import com.realestate.model.entity.Portfolio;
import com.realestate.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PortfolioDTO>> createPortfolio(
            @Valid @RequestBody CreatePortfolioRequestDTO request) {
        Portfolio portfolio = portfolioService.createPortfolio(
                request.getPortfolioName(),
                request.getRiskProfile()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(PortfolioDTO.fromEntity(portfolio), "Portfolio created successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<ApiResponse<List<PortfolioDTO>>> getMyPortfolios() {
        List<Portfolio> portfolios = portfolioService.getMyPortfolios();
        List<PortfolioDTO> dtos = portfolios.stream()
                .map(PortfolioDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos, "Portfolios retrieved"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<ApiResponse<PortfolioDTO>> getPortfolio(@PathVariable Long id) {
        Portfolio portfolio = portfolioService.getPortfolio(id);
        return ResponseEntity.ok(ApiResponse.success(PortfolioDTO.fromEntity(portfolio), "Portfolio retrieved"));
    }

    @GetMapping("/{id}/holdings")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<ApiResponse<List<HoldingDTO>>> getPortfolioHoldings(@PathVariable Long id) {
        List<Holding> holdings = portfolioService.getPortfolioHoldings(id);
        List<HoldingDTO> dtos = holdings.stream()
                .map(HoldingDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos, "Holdings retrieved"));
    }

    @GetMapping("/{id}/summary")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<ApiResponse<PortfolioService.PortfolioSummary>> getPortfolioSummary(@PathVariable Long id) {
        PortfolioService.PortfolioSummary summary = portfolioService.getPortfolioSummary(id);
        return ResponseEntity.ok(ApiResponse.success(summary, "Portfolio summary retrieved"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PortfolioDTO>> updatePortfolio(
            @PathVariable Long id,
            @Valid @RequestBody CreatePortfolioRequestDTO request) {
        Portfolio portfolio = portfolioService.updatePortfolio(
                id,
                request.getPortfolioName(),
                request.getRiskProfile()
        );
        return ResponseEntity.ok(ApiResponse.success(PortfolioDTO.fromEntity(portfolio), "Portfolio updated"));
    }

    @PostMapping("/{id}/recalculate")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> recalculatePortfolio(@PathVariable Long id) {
        portfolioService.recalculatePortfolioValues(id);
        return ResponseEntity.ok(ApiResponse.success("Recalculated", "Portfolio values recalculated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> closePortfolio(@PathVariable Long id) {
        portfolioService.closePortfolio(id);
        return ResponseEntity.ok(ApiResponse.success("Closed", "Portfolio closed successfully"));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PortfolioDTO>>> getAllPortfolios() {
        List<Portfolio> portfolios = portfolioService.getAllPortfolios();
        List<PortfolioDTO> dtos = portfolios.stream()
                .map(PortfolioDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos, "All portfolios retrieved"));
    }
}
