package com.realestate.repository;

import com.realestate.model.entity.RefreshToken;
import com.realestate.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findByFamilyId(String familyId);

    List<RefreshToken> findByUserAndStatus(User user, RefreshToken.TokenStatus status);

    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user = :user AND rt.status = 'ACTIVE' ORDER BY rt.issuedAt DESC")
    List<RefreshToken> findActiveTokensByUser(@Param("user") User user);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.status = :status WHERE rt.familyId = :familyId")
    int updateStatusByFamilyId(@Param("familyId") String familyId, @Param("status") RefreshToken.TokenStatus status);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.status = 'REVOKED' WHERE rt.user = :user AND rt.status = 'ACTIVE'")
    int revokeAllActiveTokensByUser(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now")
    int deleteExpiredTokens(@Param("now") LocalDateTime now);

    long countByUserAndStatus(User user, RefreshToken.TokenStatus status);
}
