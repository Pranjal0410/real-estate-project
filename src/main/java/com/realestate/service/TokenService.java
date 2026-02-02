package com.realestate.service;

import com.realestate.exception.UnauthorizedException;
import com.realestate.model.entity.RefreshToken;
import com.realestate.model.entity.TokenBlacklist;
import com.realestate.model.entity.User;
import com.realestate.repository.RefreshTokenRepository;
import com.realestate.repository.TokenBlacklistRepository;
import com.realestate.repository.UserRepository;
import com.realestate.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RefreshToken createRefreshToken(User user, String ipAddress, String userAgent) {
        String familyId = UUID.randomUUID().toString();
        String rawToken = UUID.randomUUID().toString();
        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(tokenHash)
                .familyId(familyId)
                .user(user)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .status(RefreshToken.TokenStatus.ACTIVE)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public TokenRotationResult rotateRefreshToken(String tokenHash, String ipAddress, String userAgent) {
        RefreshToken existingToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (existingToken.getStatus() == RefreshToken.TokenStatus.USED) {
            log.warn("Token reuse detected for family: {}", existingToken.getFamilyId());
            invalidateTokenFamily(existingToken.getFamilyId());
            throw new UnauthorizedException("Token reuse detected. All sessions invalidated.");
        }

        if (!existingToken.isActive()) {
            throw new UnauthorizedException("Refresh token is no longer valid");
        }

        existingToken.setStatus(RefreshToken.TokenStatus.USED);
        refreshTokenRepository.save(existingToken);

        String newRawToken = UUID.randomUUID().toString();
        String newTokenHash = hashToken(newRawToken);

        RefreshToken newToken = RefreshToken.builder()
                .tokenHash(newTokenHash)
                .familyId(existingToken.getFamilyId())
                .user(existingToken.getUser())
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .status(RefreshToken.TokenStatus.ACTIVE)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();

        RefreshToken savedToken = refreshTokenRepository.save(newToken);
        String accessToken = jwtTokenProvider.generateAccessToken(existingToken.getUser().getUsername());

        return new TokenRotationResult(accessToken, newTokenHash, savedToken);
    }

    public void invalidateTokenFamily(String familyId) {
        refreshTokenRepository.updateStatusByFamilyId(familyId, RefreshToken.TokenStatus.COMPROMISED);
        log.info("Invalidated token family: {}", familyId);
    }

    public void blacklistAccessToken(String jti, LocalDateTime expiresAt, TokenBlacklist.BlacklistReason reason) {
        TokenBlacklist blacklist = TokenBlacklist.builder()
                .jti(jti)
                .expiresAt(expiresAt)
                .blacklistedAt(LocalDateTime.now())
                .reason(reason)
                .build();
        tokenBlacklistRepository.save(blacklist);
    }

    public boolean isTokenBlacklisted(String jti) {
        return tokenBlacklistRepository.existsByJti(jti);
    }

    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.revokeAllActiveTokensByUser(user);
    }

    public List<RefreshToken> getActiveSessions(User user) {
        return refreshTokenRepository.findActiveTokensByUser(user);
    }

    public void revokeSession(Long tokenId, User user) {
        RefreshToken token = refreshTokenRepository.findById(tokenId)
                .orElseThrow(() -> new UnauthorizedException("Session not found"));

        if (!token.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Cannot revoke another user's session");
        }

        token.setStatus(RefreshToken.TokenStatus.REVOKED);
        refreshTokenRepository.save(token);
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedRefreshTokens = refreshTokenRepository.deleteExpiredTokens(now);
        int deletedBlacklistTokens = tokenBlacklistRepository.deleteExpiredTokens(now);
        log.info("Cleaned up {} refresh tokens and {} blacklist entries", deletedRefreshTokens, deletedBlacklistTokens);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public record TokenRotationResult(String accessToken, String refreshTokenHash, RefreshToken refreshToken) {}
}
