package com.realestate.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.model.dto.BuyTransactionRequestDTO;
import com.realestate.model.dto.CreatePortfolioRequestDTO;
import com.realestate.model.entity.Portfolio;
import com.realestate.model.entity.Property;
import com.realestate.model.entity.User;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.UserRepository;
import com.realestate.testutil.IntegrationTestBase;
import com.realestate.testutil.builder.PropertyBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InvestmentFlowIntegrationTest extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    private User testUser;
    private Property testProperty;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("investortestuser")
                .email("investor@test.com")
                .password("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG") // encoded "password"
                .firstName("Investor")
                .lastName("Test")
                .role(User.UserRole.INVESTOR)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();
        testUser = userRepository.save(testUser);

        testProperty = PropertyBuilder.aProperty()
                .withId(null)
                .withTitle("Investment Property")
                .withPrice(new BigDecimal("500000"))
                .withOwner(testUser)
                .build();
        testProperty = propertyRepository.save(testProperty);
    }

    @Test
    @DisplayName("Should complete full investment flow: create portfolio -> buy -> view holdings")
    @WithMockUser(username = "investortestuser", roles = "INVESTOR")
    void fullInvestmentFlow() throws Exception {
        // Create portfolio
        CreatePortfolioRequestDTO createRequest = CreatePortfolioRequestDTO.builder()
                .portfolioName("My Investment Portfolio")
                .riskProfile(Portfolio.RiskProfile.MODERATE)
                .build();

        String portfolioResponse = mockMvc.perform(post("/api/portfolios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.portfolioName").value("My Investment Portfolio"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract portfolio ID from response (simplified - in real test use JSON parsing)
        Long portfolioId = objectMapper.readTree(portfolioResponse).path("data").path("id").asLong();

        // Execute buy transaction
        BuyTransactionRequestDTO buyRequest = BuyTransactionRequestDTO.builder()
                .portfolioId(portfolioId)
                .propertyId(testProperty.getId())
                .quantity(new BigDecimal("1.0"))
                .idempotencyKey("test-buy-" + System.currentTimeMillis())
                .build();

        mockMvc.perform(post("/api/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.transactionType").value("BUY"))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));

        // View holdings
        mockMvc.perform(get("/api/portfolios/" + portfolioId + "/holdings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].propertyTitle").value("Investment Property"));

        // View portfolio summary
        mockMvc.perform(get("/api/portfolios/" + portfolioId + "/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.holdingsCount").value(1));
    }

    @Test
    @DisplayName("Should prevent duplicate transactions with same idempotency key")
    @WithMockUser(username = "investortestuser", roles = "INVESTOR")
    void idempotencyKeyPreventssDuplicates() throws Exception {
        // Create portfolio first
        CreatePortfolioRequestDTO createRequest = CreatePortfolioRequestDTO.builder()
                .portfolioName("Idempotency Test Portfolio")
                .riskProfile(Portfolio.RiskProfile.MODERATE)
                .build();

        String portfolioResponse = mockMvc.perform(post("/api/portfolios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long portfolioId = objectMapper.readTree(portfolioResponse).path("data").path("id").asLong();

        String idempotencyKey = "unique-key-" + System.currentTimeMillis();

        BuyTransactionRequestDTO buyRequest = BuyTransactionRequestDTO.builder()
                .portfolioId(portfolioId)
                .propertyId(testProperty.getId())
                .quantity(new BigDecimal("1.0"))
                .idempotencyKey(idempotencyKey)
                .build();

        // First request should succeed
        mockMvc.perform(post("/api/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyRequest)))
                .andExpect(status().isCreated());

        // Second request with same key should return existing transaction
        mockMvc.perform(post("/api/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.idempotencyKey").value(idempotencyKey));
    }
}
