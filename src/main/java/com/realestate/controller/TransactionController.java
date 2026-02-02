package com.realestate.controller;

import com.realestate.model.dto.*;
import com.realestate.model.entity.InvestmentTransaction;
import com.realestate.service.InvestmentTransactionService;
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
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransactionController {

    private final InvestmentTransactionService transactionService;

    @PostMapping("/buy")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionDTO>> executeBuy(
            @Valid @RequestBody BuyTransactionRequestDTO request) {
        InvestmentTransaction transaction = transactionService.executeBuyTransaction(
                request.getPortfolioId(),
                request.getPropertyId(),
                request.getQuantity(),
                request.getIdempotencyKey()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(TransactionDTO.fromEntity(transaction), "Buy transaction executed"));
    }

    @PostMapping("/sell")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionDTO>> executeSell(
            @Valid @RequestBody SellTransactionRequestDTO request) {
        InvestmentTransaction transaction = transactionService.executeSellTransaction(
                request.getPortfolioId(),
                request.getHoldingId(),
                request.getQuantity(),
                request.getIdempotencyKey()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(TransactionDTO.fromEntity(transaction), "Sell transaction executed"));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionDTO>> executeTransfer(
            @Valid @RequestBody TransferRequestDTO request) {
        InvestmentTransaction transaction = transactionService.executeTransfer(
                request.getFromPortfolioId(),
                request.getToPortfolioId(),
                request.getHoldingId(),
                request.getQuantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(TransactionDTO.fromEntity(transaction), "Transfer executed"));
    }

    @GetMapping("/portfolio/{portfolioId}")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionHistory(
            @PathVariable Long portfolioId) {
        List<InvestmentTransaction> transactions = transactionService.getTransactionHistory(portfolioId);
        List<TransactionDTO> dtos = transactions.stream()
                .map(TransactionDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos, "Transaction history retrieved"));
    }

    @GetMapping("/{reference}")
    @PreAuthorize("hasRole('INVESTOR') or hasRole('ADMIN') or hasRole('ANALYST')")
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransaction(
            @PathVariable String reference) {
        InvestmentTransaction transaction = transactionService.getTransaction(reference);
        return ResponseEntity.ok(ApiResponse.success(TransactionDTO.fromEntity(transaction), "Transaction retrieved"));
    }

    @PostMapping("/admin/{id}/reverse")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TransactionDTO>> reverseTransaction(@PathVariable Long id) {
        InvestmentTransaction transaction = transactionService.reverseTransaction(id);
        return ResponseEntity.ok(ApiResponse.success(TransactionDTO.fromEntity(transaction), "Transaction reversed"));
    }
}
