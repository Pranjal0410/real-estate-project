package com.realestate.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDTO {

    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long expiresIn; // Token expiry in seconds
    private LocalDateTime expiresAt;

    private UserDTO user;

    private String message;
    private Boolean success;

    // Additional authentication info
    private Boolean isFirstLogin;
    private Boolean requiresPasswordChange;
    private String redirectUrl;

    // For 2FA scenarios
    private Boolean requiresTwoFactor;
    private String twoFactorMethod;
}