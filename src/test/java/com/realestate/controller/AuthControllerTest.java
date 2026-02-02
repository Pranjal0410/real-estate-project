package com.realestate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.model.dto.AuthRequestDTO;
import com.realestate.model.dto.AuthResponseDTO;
import com.realestate.model.dto.TokenRefreshRequestDTO;
import com.realestate.model.dto.UserDTO;
import com.realestate.model.entity.RefreshToken;
import com.realestate.model.entity.User;
import com.realestate.repository.UserRepository;
import com.realestate.security.JwtTokenProvider;
import com.realestate.service.RateLimitService;
import com.realestate.service.TokenService;
import com.realestate.service.UserService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private RateLimitService rateLimitService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserRepository userRepository;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .role(User.UserRole.INVESTOR)
                .build();
        testUser.setId(1L);

        testUserDTO = UserDTO.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .role(UserDTO.UserRole.INVESTOR)
                .build();
    }

    @Test
    @DisplayName("Should authenticate user successfully")
    void login_Success() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("testuser");
        request.setPassword("password");

        AuthResponseDTO response = AuthResponseDTO.builder()
                .accessToken("access-token")
                .user(testUserDTO)
                .success(true)
                .build();

        RefreshToken refreshToken = RefreshToken.builder()
                .familyId("family-id")
                .user(testUser)
                .build();

        when(userService.authenticate(any())).thenReturn(response);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(tokenService.createRefreshToken(any(), any(), any())).thenReturn(refreshToken);
        when(jwtTokenProvider.generateRefreshToken(any(), any())).thenReturn("refresh-token");
        when(jwtTokenProvider.getAccessTokenExpirationMs()).thenReturn(900000L);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

    @Test
    @DisplayName("Should register user successfully")
    void register_Success() throws Exception {
        UserDTO request = UserDTO.builder()
                .username("newuser")
                .email("new@example.com")
                .password("password123")
                .firstName("New")
                .lastName("User")
                .build();

        when(userService.register(any())).thenReturn(testUserDTO);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should refresh token successfully")
    void refreshToken_Success() throws Exception {
        TokenRefreshRequestDTO request = new TokenRefreshRequestDTO("valid-refresh-token");

        when(rateLimitService.tryAcquire(any())).thenReturn(true);
        when(jwtTokenProvider.validateToken("valid-refresh-token")).thenReturn(true);
        when(jwtTokenProvider.isRefreshToken("valid-refresh-token")).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromToken("valid-refresh-token")).thenReturn("testuser");
        when(jwtTokenProvider.getFamilyId("valid-refresh-token")).thenReturn("family-id");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(tokenService.createRefreshToken(any(), any(), any())).thenReturn(
                RefreshToken.builder().familyId("new-family").build());
        when(jwtTokenProvider.generateAccessToken("testuser")).thenReturn("new-access-token");
        when(jwtTokenProvider.generateRefreshToken(any(), any())).thenReturn("new-refresh-token");
        when(jwtTokenProvider.getAccessTokenExpirationMs()).thenReturn(900000L);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("new-refresh-token"));
    }

    @Test
    @DisplayName("Should return 429 when rate limited")
    void refreshToken_RateLimited() throws Exception {
        TokenRefreshRequestDTO request = new TokenRefreshRequestDTO("valid-refresh-token");

        when(rateLimitService.tryAcquire(any())).thenReturn(false);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @DisplayName("Should logout successfully")
    @WithMockUser
    void logout_Success() throws Exception {
        when(jwtTokenProvider.getJtiFromToken(any())).thenReturn("jti-123");
        when(jwtTokenProvider.getExpirationDateFromToken(any())).thenReturn(new java.util.Date(System.currentTimeMillis() + 3600000));

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should get active sessions")
    @WithMockUser(username = "testuser")
    void getSessions_Success() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(tokenService.getActiveSessions(testUser)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should revoke session")
    @WithMockUser(username = "testuser")
    void revokeSession_Success() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        mockMvc.perform(delete("/api/auth/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should get current user")
    @WithMockUser
    void getCurrentUser_Success() throws Exception {
        when(userService.getCurrentUser()).thenReturn(testUserDTO);

        mockMvc.perform(get("/api/auth/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }
}
