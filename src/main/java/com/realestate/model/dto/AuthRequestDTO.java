package com.realestate.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {

    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Builder.Default
    private Boolean rememberMe = false;

    // For registration
    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Size(max = 50, message = "First name must be maximum 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be maximum 50 characters")
    private String lastName;

    private String phoneNumber;
    private UserRole role;

    // For password reset
    private String resetToken;
    private String newPassword;

    public enum UserRole {
        ADMIN, INVESTOR, PROPERTY_OWNER, AGENT
    }
}