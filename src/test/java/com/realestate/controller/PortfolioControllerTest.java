package com.realestate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.model.dto.CreatePortfolioRequestDTO;
import com.realestate.model.entity.Portfolio;
import com.realestate.model.entity.User;
import com.realestate.service.PortfolioService;
import com.realestate.testutil.builder.PortfolioBuilder;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PortfolioController.class)
@AutoConfigureMockMvc(addFilters = false)
class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PortfolioService portfolioService;

    private User testUser;
    private Portfolio testPortfolio;

    @BeforeEach
    void setUp() {
        testUser = UserBuilder.aUser().withId(1L).asInvestor().build();
        testPortfolio = PortfolioBuilder.aPortfolio()
                .withId(1L)
                .withUser(testUser)
                .withName("Test Portfolio")
                .build();
    }

    @Test
    @DisplayName("Should create portfolio successfully")
    @WithMockUser(roles = "INVESTOR")
    void createPortfolio_Success() throws Exception {
        CreatePortfolioRequestDTO request = CreatePortfolioRequestDTO.builder()
                .portfolioName("My Portfolio")
                .riskProfile(Portfolio.RiskProfile.MODERATE)
                .build();

        when(portfolioService.createPortfolio(eq("My Portfolio"), any())).thenReturn(testPortfolio);

        mockMvc.perform(post("/api/portfolios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.portfolioName").value("Test Portfolio"));
    }

    @Test
    @DisplayName("Should get user's portfolios")
    @WithMockUser(roles = "INVESTOR")
    void getMyPortfolios_Success() throws Exception {
        when(portfolioService.getMyPortfolios()).thenReturn(List.of(testPortfolio));

        mockMvc.perform(get("/api/portfolios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].portfolioName").value("Test Portfolio"));
    }

    @Test
    @DisplayName("Should get portfolio by ID")
    @WithMockUser(roles = "INVESTOR")
    void getPortfolio_Success() throws Exception {
        when(portfolioService.getPortfolio(1L)).thenReturn(testPortfolio);

        mockMvc.perform(get("/api/portfolios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("Should get portfolio holdings")
    @WithMockUser(roles = "INVESTOR")
    void getPortfolioHoldings_Success() throws Exception {
        when(portfolioService.getPortfolioHoldings(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/portfolios/1/holdings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Should get portfolio summary")
    @WithMockUser(roles = "INVESTOR")
    void getPortfolioSummary_Success() throws Exception {
        PortfolioService.PortfolioSummary summary = new PortfolioService.PortfolioSummary(
                1L, "Test Portfolio", BigDecimal.valueOf(100000), BigDecimal.valueOf(110000),
                BigDecimal.valueOf(10000), BigDecimal.ZERO, 5, Portfolio.RiskProfile.MODERATE
        );

        when(portfolioService.getPortfolioSummary(1L)).thenReturn(summary);

        mockMvc.perform(get("/api/portfolios/1/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Test Portfolio"))
                .andExpect(jsonPath("$.data.holdingsCount").value(5));
    }

    @Test
    @DisplayName("Should update portfolio")
    @WithMockUser(roles = "INVESTOR")
    void updatePortfolio_Success() throws Exception {
        CreatePortfolioRequestDTO request = CreatePortfolioRequestDTO.builder()
                .portfolioName("Updated Name")
                .riskProfile(Portfolio.RiskProfile.AGGRESSIVE)
                .build();

        Portfolio updatedPortfolio = PortfolioBuilder.aPortfolio()
                .withId(1L)
                .withUser(testUser)
                .withName("Updated Name")
                .aggressive()
                .build();

        when(portfolioService.updatePortfolio(eq(1L), eq("Updated Name"), any())).thenReturn(updatedPortfolio);

        mockMvc.perform(put("/api/portfolios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should close portfolio")
    @WithMockUser(roles = "INVESTOR")
    void closePortfolio_Success() throws Exception {
        mockMvc.perform(delete("/api/portfolios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should recalculate portfolio")
    @WithMockUser(roles = "INVESTOR")
    void recalculatePortfolio_Success() throws Exception {
        mockMvc.perform(post("/api/portfolios/1/recalculate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Admin should get all portfolios")
    @WithMockUser(roles = "ADMIN")
    void getAllPortfolios_Admin_Success() throws Exception {
        when(portfolioService.getAllPortfolios()).thenReturn(List.of(testPortfolio));

        mockMvc.perform(get("/api/portfolios/admin/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
}
