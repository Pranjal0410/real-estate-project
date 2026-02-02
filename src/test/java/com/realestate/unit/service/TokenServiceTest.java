package com.realestate.unit.service;

import com.realestate.exception.UnauthorizedException;
import com.realestate.model.entity.RefreshToken;
import com.realestate.model.entity.TokenBlacklist;
import com.realestate.model.entity.User;
import com.realestate.repository.RefreshTokenRepository;
import com.realestate.repository.TokenBlacklistRepository;
import com.realestate.repository.UserRepository;
import com.realestate.security.JwtTokenProvider;
import com.realestate.service.TokenService;
import com.realestate.testutil.builder.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private TokenService tokenService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = UserBuilder.aUser().withId(1L).asInvestor().build();
    }

    @Test
    @DisplayName("Should create refresh token successfully")
    void createRefreshToken_Success() {
        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken result = tokenService.createRefreshToken(testUser, "127.0.0.1", "Test Agent");

        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(testUser);
        assertThat(result.getStatus()).isEqualTo(RefreshToken.TokenStatus.ACTIVE);
        assertThat(result.getIpAddress()).isEqualTo("127.0.0.1");
        assertThat(result.getUserAgent()).isEqualTo("Test Agent");
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Should blacklist access token successfully")
    void blacklistAccessToken_Success() {
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
        when(tokenBlacklistRepository.save(any(TokenBlacklist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        tokenService.blacklistAccessToken("test-jti", expiresAt, TokenBlacklist.BlacklistReason.LOGOUT);

        verify(tokenBlacklistRepository).save(any(TokenBlacklist.class));
    }

    @Test
    @DisplayName("Should return true when token is blacklisted")
    void isTokenBlacklisted_ReturnsTrue() {
        when(tokenBlacklistRepository.existsByJti("blacklisted-jti")).thenReturn(true);

        boolean result = tokenService.isTokenBlacklisted("blacklisted-jti");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when token is not blacklisted")
    void isTokenBlacklisted_ReturnsFalse() {
        when(tokenBlacklistRepository.existsByJti("valid-jti")).thenReturn(false);

        boolean result = tokenService.isTokenBlacklisted("valid-jti");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should revoke all user tokens")
    void revokeAllUserTokens_Success() {
        when(refreshTokenRepository.revokeAllActiveTokensByUser(testUser)).thenReturn(3);

        tokenService.revokeAllUserTokens(testUser);

        verify(refreshTokenRepository).revokeAllActiveTokensByUser(testUser);
    }

    @Test
    @DisplayName("Should get active sessions for user")
    void getActiveSessions_Success() {
        RefreshToken token1 = RefreshToken.builder()
                .user(testUser)
                .status(RefreshToken.TokenStatus.ACTIVE)
                .build();
        RefreshToken token2 = RefreshToken.builder()
                .user(testUser)
                .status(RefreshToken.TokenStatus.ACTIVE)
                .build();

        when(refreshTokenRepository.findActiveTokensByUser(testUser))
                .thenReturn(List.of(token1, token2));

        List<RefreshToken> result = tokenService.getActiveSessions(testUser);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should revoke session for own user")
    void revokeSession_Success() {
        RefreshToken token = RefreshToken.builder()
                .user(testUser)
                .status(RefreshToken.TokenStatus.ACTIVE)
                .build();
        token.setId(1L);

        when(refreshTokenRepository.findById(1L)).thenReturn(Optional.of(token));
        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        tokenService.revokeSession(1L, testUser);

        assertThat(token.getStatus()).isEqualTo(RefreshToken.TokenStatus.REVOKED);
        verify(refreshTokenRepository).save(token);
    }

    @Test
    @DisplayName("Should throw exception when revoking another user's session")
    void revokeSession_ThrowsException_WhenNotOwner() {
        User otherUser = UserBuilder.aUser().withId(2L).build();
        RefreshToken token = RefreshToken.builder()
                .user(otherUser)
                .status(RefreshToken.TokenStatus.ACTIVE)
                .build();
        token.setId(1L);

        when(refreshTokenRepository.findById(1L)).thenReturn(Optional.of(token));

        assertThatThrownBy(() -> tokenService.revokeSession(1L, testUser))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Cannot revoke another user's session");
    }

    @Test
    @DisplayName("Should throw exception when session not found")
    void revokeSession_ThrowsException_WhenNotFound() {
        when(refreshTokenRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tokenService.revokeSession(999L, testUser))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Session not found");
    }

    @Test
    @DisplayName("Should invalidate token family")
    void invalidateTokenFamily_Success() {
        String familyId = "test-family-id";
        when(refreshTokenRepository.updateStatusByFamilyId(familyId, RefreshToken.TokenStatus.COMPROMISED))
                .thenReturn(2);

        tokenService.invalidateTokenFamily(familyId);

        verify(refreshTokenRepository).updateStatusByFamilyId(familyId, RefreshToken.TokenStatus.COMPROMISED);
    }
}
