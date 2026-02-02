package com.realestate.repository;

import com.realestate.model.entity.RefreshToken;
import com.realestate.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RefreshTokenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .role(User.UserRole.INVESTOR)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();
        entityManager.persist(testUser);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should save refresh token successfully")
    void save_Success() {
        RefreshToken token = createToken("hash1", "family1", RefreshToken.TokenStatus.ACTIVE);

        RefreshToken saved = refreshTokenRepository.save(token);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTokenHash()).isEqualTo("hash1");
    }

    @Test
    @DisplayName("Should find token by hash")
    void findByTokenHash_Success() {
        RefreshToken token = createAndPersistToken("unique-hash", "family1");

        Optional<RefreshToken> result = refreshTokenRepository.findByTokenHash("unique-hash");

        assertThat(result).isPresent();
        assertThat(result.get().getFamilyId()).isEqualTo("family1");
    }

    @Test
    @DisplayName("Should find tokens by family ID")
    void findByFamilyId_Success() {
        createAndPersistToken("hash1", "same-family");
        createAndPersistToken("hash2", "same-family");
        createAndPersistToken("hash3", "different-family");

        List<RefreshToken> result = refreshTokenRepository.findByFamilyId("same-family");

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should find active tokens by user")
    void findActiveTokensByUser_Success() {
        createAndPersistToken("hash1", "f1", RefreshToken.TokenStatus.ACTIVE);
        createAndPersistToken("hash2", "f2", RefreshToken.TokenStatus.ACTIVE);
        createAndPersistToken("hash3", "f3", RefreshToken.TokenStatus.REVOKED);

        List<RefreshToken> result = refreshTokenRepository.findActiveTokensByUser(testUser);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should count tokens by user and status")
    void countByUserAndStatus_Success() {
        createAndPersistToken("h1", "f1", RefreshToken.TokenStatus.ACTIVE);
        createAndPersistToken("h2", "f2", RefreshToken.TokenStatus.ACTIVE);
        createAndPersistToken("h3", "f3", RefreshToken.TokenStatus.USED);

        long activeCount = refreshTokenRepository.countByUserAndStatus(testUser, RefreshToken.TokenStatus.ACTIVE);
        long usedCount = refreshTokenRepository.countByUserAndStatus(testUser, RefreshToken.TokenStatus.USED);

        assertThat(activeCount).isEqualTo(2);
        assertThat(usedCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Should find tokens by user and status")
    void findByUserAndStatus_Success() {
        createAndPersistToken("h1", "f1", RefreshToken.TokenStatus.ACTIVE);
        createAndPersistToken("h2", "f2", RefreshToken.TokenStatus.REVOKED);

        List<RefreshToken> activeTokens = refreshTokenRepository.findByUserAndStatus(
                testUser, RefreshToken.TokenStatus.ACTIVE);

        assertThat(activeTokens).hasSize(1);
        assertThat(activeTokens.get(0).getStatus()).isEqualTo(RefreshToken.TokenStatus.ACTIVE);
    }

    private RefreshToken createToken(String hash, String familyId, RefreshToken.TokenStatus status) {
        return RefreshToken.builder()
                .tokenHash(hash)
                .familyId(familyId)
                .user(testUser)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .status(status)
                .ipAddress("127.0.0.1")
                .userAgent("Test Agent")
                .build();
    }

    private RefreshToken createAndPersistToken(String hash, String familyId) {
        return createAndPersistToken(hash, familyId, RefreshToken.TokenStatus.ACTIVE);
    }

    private RefreshToken createAndPersistToken(String hash, String familyId, RefreshToken.TokenStatus status) {
        RefreshToken token = createToken(hash, familyId, status);
        entityManager.persist(token);
        entityManager.flush();
        return token;
    }
}
