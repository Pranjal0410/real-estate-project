package com.realestate.controller;

import com.realestate.model.dto.*;
import com.realestate.model.entity.RefreshToken;
import com.realestate.model.entity.TokenBlacklist;
import com.realestate.model.entity.User;
import com.realestate.repository.UserRepository;
import com.realestate.security.JwtTokenProvider;
import com.realestate.service.RateLimitService;
import com.realestate.service.TokenService;
import com.realestate.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final RateLimitService rateLimitService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> authenticate(
            @Valid @RequestBody AuthRequestDTO authRequest,
            HttpServletRequest request) {
        log.info("Authentication request for user: {}", authRequest.getUsername());

        AuthResponseDTO response = userService.authenticate(authRequest);

        User user = userRepository.findByUsername(response.getUser().getUsername())
                .orElseThrow();

        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        RefreshToken refreshToken = tokenService.createRefreshToken(user, ipAddress, userAgent);

        String jwtRefreshToken = jwtTokenProvider.generateRefreshToken(
                user.getUsername(), refreshToken.getFamilyId());

        response.setRefreshToken(jwtRefreshToken);
        response.setExpiresIn(jwtTokenProvider.getAccessTokenExpirationMs() / 1000);

        return ResponseEntity.ok(ApiResponse.success(response, "Authentication successful"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(
            @Valid @RequestBody UserDTO userDTO) {
        log.info("Registration request for user: {}", userDTO.getUsername());
        UserDTO registered = userService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(registered, "Registration successful"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponseDTO>> refreshToken(
            @Valid @RequestBody TokenRefreshRequestDTO refreshRequest,
            HttpServletRequest request) {
        String clientIp = getClientIp(request);

        if (!rateLimitService.tryAcquire("refresh:" + clientIp)) {
            log.warn("Rate limit exceeded for token refresh from IP: {}", clientIp);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(ApiResponse.error("Too many refresh attempts. Please try again later."));
        }

        String token = refreshRequest.getRefreshToken();

        if (!jwtTokenProvider.validateToken(token) || !jwtTokenProvider.isRefreshToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid refresh token"));
        }

        String username = jwtTokenProvider.getUsernameFromToken(token);
        String familyId = jwtTokenProvider.getFamilyId(token);

        String ipAddress = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken newRefreshToken = tokenService.createRefreshToken(user, ipAddress, userAgent);
        String newAccessToken = jwtTokenProvider.generateAccessToken(username);
        String newRefreshJwt = jwtTokenProvider.generateRefreshToken(username, newRefreshToken.getFamilyId());

        TokenRefreshResponseDTO response = TokenRefreshResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshJwt)
                .expiresIn(jwtTokenProvider.getAccessTokenExpirationMs() / 1000)
                .build();

        log.info("Token refreshed for user: {}", username);
        return ResponseEntity.ok(ApiResponse.success(response, "Token refreshed successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String jti = jwtTokenProvider.getJtiFromToken(token);
            LocalDateTime expiresAt = jwtTokenProvider.getExpirationDateFromToken(token)
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            tokenService.blacklistAccessToken(jti, expiresAt, TokenBlacklist.BlacklistReason.LOGOUT);
            log.info("User logged out, token blacklisted");
        }

        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", "Logout successful"));
    }

    @GetMapping("/sessions")
    public ResponseEntity<ApiResponse<List<SessionDTO>>> getActiveSessions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<RefreshToken> sessions = tokenService.getActiveSessions(user);
        List<SessionDTO> sessionDTOs = sessions.stream()
                .map(s -> SessionDTO.fromEntity(s, false))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(sessionDTOs, "Active sessions retrieved"));
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<ApiResponse<String>> revokeSession(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        tokenService.revokeSession(id, user);
        return ResponseEntity.ok(ApiResponse.success("Session revoked", "Session revoked successfully"));
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {
        UserDTO currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(currentUser, "Current user fetched"));
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
