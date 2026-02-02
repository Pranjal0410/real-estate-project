package com.realestate.unit.security;

import com.realestate.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret",
                "ThisIsAVerySecureSecretKeyForJWTTokenGenerationMinimum256Bits");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", 86400000L);
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenExpiration", 900000L);
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshTokenExpiration", 604800000L);
    }

    @Test
    @DisplayName("Should generate access token successfully")
    void generateAccessToken_Success() {
        String token = jwtTokenProvider.generateAccessToken("testuser");

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("Should generate refresh token successfully")
    void generateRefreshToken_Success() {
        String token = jwtTokenProvider.generateRefreshToken("testuser", "family-id-123");

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("Should extract username from token")
    void getUsernameFromToken_Success() {
        String token = jwtTokenProvider.generateAccessToken("testuser");

        String username = jwtTokenProvider.getUsernameFromToken(token);

        assertThat(username).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should extract JTI from token")
    void getJtiFromToken_Success() {
        String token = jwtTokenProvider.generateAccessToken("testuser");

        String jti = jwtTokenProvider.getJtiFromToken(token);

        assertThat(jti).isNotNull();
        assertThat(jti).isNotEmpty();
    }

    @Test
    @DisplayName("Should identify access token type")
    void isAccessToken_Success() {
        String accessToken = jwtTokenProvider.generateAccessToken("testuser");

        assertThat(jwtTokenProvider.isAccessToken(accessToken)).isTrue();
        assertThat(jwtTokenProvider.isRefreshToken(accessToken)).isFalse();
    }

    @Test
    @DisplayName("Should identify refresh token type")
    void isRefreshToken_Success() {
        String refreshToken = jwtTokenProvider.generateRefreshToken("testuser", "family-id");

        assertThat(jwtTokenProvider.isRefreshToken(refreshToken)).isTrue();
        assertThat(jwtTokenProvider.isAccessToken(refreshToken)).isFalse();
    }

    @Test
    @DisplayName("Should extract token type from access token")
    void getTokenType_AccessToken() {
        String token = jwtTokenProvider.generateAccessToken("testuser");

        String type = jwtTokenProvider.getTokenType(token);

        assertThat(type).isEqualTo("access");
    }

    @Test
    @DisplayName("Should extract token type from refresh token")
    void getTokenType_RefreshToken() {
        String token = jwtTokenProvider.generateRefreshToken("testuser", "family-id");

        String type = jwtTokenProvider.getTokenType(token);

        assertThat(type).isEqualTo("refresh");
    }

    @Test
    @DisplayName("Should extract family ID from refresh token")
    void getFamilyId_Success() {
        String token = jwtTokenProvider.generateRefreshToken("testuser", "my-family-id");

        String familyId = jwtTokenProvider.getFamilyId(token);

        assertThat(familyId).isEqualTo("my-family-id");
    }

    @Test
    @DisplayName("Should validate valid token")
    void validateToken_Valid() {
        String token = jwtTokenProvider.generateAccessToken("testuser");

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should invalidate malformed token")
    void validateToken_Invalid() {
        boolean isValid = jwtTokenProvider.validateToken("invalid.token.here");

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should return false for null token")
    void validateToken_Null() {
        boolean isValid = jwtTokenProvider.validateToken(null);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should check if token is not expired")
    void isTokenExpired_NotExpired() {
        String token = jwtTokenProvider.generateAccessToken("testuser");

        boolean isExpired = jwtTokenProvider.isTokenExpired(token);

        assertThat(isExpired).isFalse();
    }

    @Test
    @DisplayName("Should get expiration date from token")
    void getExpirationDateFromToken_Success() {
        String token = jwtTokenProvider.generateAccessToken("testuser");

        java.util.Date expiration = jwtTokenProvider.getExpirationDateFromToken(token);

        assertThat(expiration).isNotNull();
        assertThat(expiration).isAfter(new java.util.Date());
    }

    @Test
    @DisplayName("Should refresh token successfully")
    void refreshToken_Success() {
        String originalToken = jwtTokenProvider.generateAccessToken("testuser");

        String newToken = jwtTokenProvider.refreshToken(originalToken);

        assertThat(newToken).isNotNull();
        assertThat(newToken).isNotEqualTo(originalToken);
        assertThat(jwtTokenProvider.getUsernameFromToken(newToken)).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should return null when refreshing invalid token")
    void refreshToken_Invalid() {
        String newToken = jwtTokenProvider.refreshToken("invalid.token");

        assertThat(newToken).isNull();
    }

    @Test
    @DisplayName("Should get access token expiration milliseconds")
    void getAccessTokenExpirationMs_Success() {
        long expiration = jwtTokenProvider.getAccessTokenExpirationMs();

        assertThat(expiration).isEqualTo(900000L);
    }

    @Test
    @DisplayName("Should get refresh token expiration milliseconds")
    void getRefreshTokenExpirationMs_Success() {
        long expiration = jwtTokenProvider.getRefreshTokenExpirationMs();

        assertThat(expiration).isEqualTo(604800000L);
    }
}
