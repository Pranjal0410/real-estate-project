package com.realestate.model.dto;

import com.realestate.model.entity.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {

    private Long id;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private RefreshToken.TokenStatus status;
    private boolean current;

    public static SessionDTO fromEntity(RefreshToken token, boolean isCurrent) {
        return SessionDTO.builder()
                .id(token.getId())
                .ipAddress(token.getIpAddress())
                .userAgent(token.getUserAgent())
                .issuedAt(token.getIssuedAt())
                .expiresAt(token.getExpiresAt())
                .status(token.getStatus())
                .current(isCurrent)
                .build();
    }
}
