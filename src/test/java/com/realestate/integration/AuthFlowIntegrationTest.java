package com.realestate.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.model.dto.AuthRequestDTO;
import com.realestate.model.dto.UserDTO;
import com.realestate.testutil.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthFlowIntegrationTest extends IntegrationTestBase {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should complete full registration and login flow")
    void fullAuthFlow() throws Exception {
        // Register
        UserDTO registerRequest = UserDTO.builder()
                .username("integrationuser")
                .email("integration@test.com")
                .password("password123")
                .firstName("Integration")
                .lastName("Test")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("integrationuser"));

        // Login
        AuthRequestDTO loginRequest = new AuthRequestDTO();
        loginRequest.setUsername("integrationuser");
        loginRequest.setPassword("password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andReturn();

        // Extract token from response
        String responseBody = loginResult.getResponse().getContentAsString();
        assertThat(responseBody).contains("accessToken");
    }

    @Test
    @DisplayName("Should fail login with wrong password")
    void loginWithWrongPassword() throws Exception {
        // First register
        UserDTO registerRequest = UserDTO.builder()
                .username("wrongpassuser")
                .email("wrongpass@test.com")
                .password("correctpassword")
                .firstName("Wrong")
                .lastName("Pass")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        // Try login with wrong password
        AuthRequestDTO loginRequest = new AuthRequestDTO();
        loginRequest.setUsername("wrongpassuser");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should fail registration with duplicate username")
    void registerDuplicateUsername() throws Exception {
        UserDTO firstUser = UserDTO.builder()
                .username("duplicateuser")
                .email("first@test.com")
                .password("password123")
                .firstName("First")
                .lastName("User")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isCreated());

        UserDTO duplicateUser = UserDTO.builder()
                .username("duplicateuser")
                .email("second@test.com")
                .password("password123")
                .firstName("Second")
                .lastName("User")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isConflict());
    }
}
