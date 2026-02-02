package com.realestate.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "token_blacklist",
       indexes = {
           @Index(name = "idx_jti", columnList = "jti"),
           @Index(name = "idx_expires_at", columnList = "expires_at")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jti", nullable = false, unique = true, length = 36)
    private String jti;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private BlacklistReason reason;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public enum BlacklistReason {
        LOGOUT,
        PASSWORD_CHANGE,
        TOKEN_ROTATION,
        SECURITY_BREACH,
        ADMIN_REVOKE
    }
}
