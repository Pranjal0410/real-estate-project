package com.realestate.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @Pattern(regexp = "^$|^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phoneNumber;

    private UserRole role;

    private Boolean isEnabled;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;

    private String address;
    private String profilePictureUrl;
    private LocalDateTime lastLogin;

    private Map<String, String> preferences;
    
    // Only used for registration, not stored in response
    private String password;

    // Statistics (read-only)
    private Integer totalProperties;
    private Integer totalCalculations;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum UserRole {
        ADMIN, INVESTOR, PROPERTY_OWNER, AGENT
    }
}