package com.realestate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.model.dto.BuyTransactionRequestDTO;
import com.realestate.model.dto.SellTransactionRequestDTO;
import com.realestate.model.dto.TransferRequestDTO;
import com.realestate.model.entity.*;
import com.realestate.service.InvestmentTransactionService;
import com.realestate.testutil.builder.PortfolioBuilder;
import com.realestate.testutil.builder.PropertyBuilder;
import com.realestate.testutil.builder.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InvestmentTransactionService transactionService;

    private User testUser;
    private Portfolio testPortfolio;
    private Property testProperty;
    private InvestmentTransaction testTransaction;

    @BeforeEach
    void setUp() {
        testUser = UserBuilder.aUser().withId(1L).asInvestor().build();
        testPortfolio = PortfolioBuilder.aPortfolio().withId(1L).withUser(testUser).build();
        testProperty = PropertyBuilder.aProperty().withId(1L).withOwner(testUser).build();

        testTransaction = InvestmentTransaction.builder()
                .transactionReference("TXN-20240101-00001")
                .portfolio(testPortfolio)
                .property(testProperty)
                .user(testUser)
                .transactionType(InvestmentTransaction.TransactionType.BUY)
                .quantity(new BigDecimal("1.0"))
                .unitPrice(new BigDecimal("500000"))
                .grossAmount(new BigDecimal("500000"))
                .netAmount(new BigDecimal("505000"))
                .platformFee(new BigDecimal("5000"))
                .status(InvestmentTransaction.TransactionStatus.COMPLETED)
                .build();
        testTransaction.setId(1L);
    }

    @Test
    @DisplayName("Should execute buy transaction successfully")
    @WithMockUser(roles = "INVESTOR")
    void executeBuy_Success() throws Exception {
        BuyTransactionRequestDTO request = BuyTransactionRequestDTO.builder()
                .portfolioId(1L)
                .propertyId(1L)
                .quantity(new BigDecimal("1.0"))
                .idempotencyKey("idem-key-1")
                .build();

        when(transactionService.executeBuyTransaction(eq(1L), eq(1L), any(), eq("idem-key-1")))
                .thenReturn(testTransaction);

        mockMvc.perform(post("/api/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.transactionType").value("BUY"))
                .andExpect(jsonPath("$.data.transactionReference").exists());
    }

    @Test
    @DisplayName("Should execute sell transaction successfully")
    @WithMockUser(roles = "INVESTOR")
    void executeSell_Success() throws Exception {
        SellTransactionRequestDTO request = SellTransactionRequestDTO.builder()
                .portfolioId(1L)
                .holdingId(1L)
                .quantity(new BigDecimal("0.5"))
                .build();

        InvestmentTransaction sellTransaction = InvestmentTransaction.builder()
                .transactionReference("TXN-20240101-00002")
                .portfolio(testPortfolio)
                .property(testProperty)
                .user(testUser)
                .transactionType(InvestmentTransaction.TransactionType.SELL)
                .quantity(new BigDecimal("0.5"))
                .unitPrice(new BigDecimal("550000"))
                .grossAmount(new BigDecimal("275000"))
                .netAmount(new BigDecimal("272250"))
                .realizedGainLoss(new BigDecimal("25000"))
                .status(InvestmentTransaction.TransactionStatus.COMPLETED)
                .build();
        sellTransaction.setId(2L);

        when(transactionService.executeSellTransaction(eq(1L), eq(1L), any(), isNull()))
                .thenReturn(sellTransaction);

        mockMvc.perform(post("/api/transactions/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.transactionType").value("SELL"));
    }

    @Test
    @DisplayName("Should execute transfer successfully")
    @WithMockUser(roles = "INVESTOR")
    void executeTransfer_Success() throws Exception {
        TransferRequestDTO request = TransferRequestDTO.builder()
                .fromPortfolioId(1L)
                .toPortfolioId(2L)
                .holdingId(1L)
                .quantity(new BigDecimal("0.5"))
                .build();

        InvestmentTransaction transferTransaction = InvestmentTransaction.builder()
                .transactionReference("TXN-20240101-00003")
                .portfolio(testPortfolio)
                .property(testProperty)
                .user(testUser)
                .transactionType(InvestmentTransaction.TransactionType.TRANSFER_OUT)
                .quantity(new BigDecimal("0.5"))
                .status(InvestmentTransaction.TransactionStatus.COMPLETED)
                .build();
        transferTransaction.setId(3L);

        when(transactionService.executeTransfer(eq(1L), eq(2L), eq(1L), any()))
                .thenReturn(transferTransaction);

        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.transactionType").value("TRANSFER_OUT"));
    }

    @Test
    @DisplayName("Should get transaction history")
    @WithMockUser(roles = "INVESTOR")
    void getTransactionHistory_Success() throws Exception {
        when(transactionService.getTransactionHistory(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/transactions/portfolio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Should get transaction by reference")
    @WithMockUser(roles = "INVESTOR")
    void getTransaction_Success() throws Exception {
        when(transactionService.getTransaction("TXN-20240101-00001")).thenReturn(testTransaction);

        mockMvc.perform(get("/api/transactions/TXN-20240101-00001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.transactionReference").value("TXN-20240101-00001"));
    }

    @Test
    @DisplayName("Admin should reverse transaction")
    @WithMockUser(roles = "ADMIN")
    void reverseTransaction_Admin_Success() throws Exception {
        InvestmentTransaction reversedTransaction = InvestmentTransaction.builder()
                .transactionReference("TXN-20240101-00001")
                .portfolio(testPortfolio)
                .property(testProperty)
                .user(testUser)
                .transactionType(InvestmentTransaction.TransactionType.BUY)
                .status(InvestmentTransaction.TransactionStatus.REVERSED)
                .build();
        reversedTransaction.setId(1L);

        when(transactionService.reverseTransaction(1L)).thenReturn(reversedTransaction);

        mockMvc.perform(post("/api/transactions/admin/1/reverse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("REVERSED"));
    }

    @Test
    @DisplayName("Should validate buy request - missing portfolioId")
    @WithMockUser(roles = "INVESTOR")
    void executeBuy_ValidationError() throws Exception {
        BuyTransactionRequestDTO request = BuyTransactionRequestDTO.builder()
                .propertyId(1L)
                .quantity(new BigDecimal("1.0"))
                .build();

        mockMvc.perform(post("/api/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
