package com.realestate.controller;

import com.realestate.model.dto.ApiResponse;
import com.realestate.model.dto.AuthRequestDTO;
import com.realestate.model.dto.AuthResponseDTO;
import com.realestate.model.dto.UserDTO;
import com.realestate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> authenticate(
            @Valid @RequestBody AuthRequestDTO authRequest) {
        log.info("Authentication request for user: {}", authRequest.getUsername());
        AuthResponseDTO response = userService.authenticate(authRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Authentication successful"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(
            @Valid @RequestBody UserDTO userDTO) {
        log.info("Registration request for user: {}", userDTO.getUsername());
        UserDTO registered = userService.register(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(registered, "Registration successful"));
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {
        UserDTO currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(currentUser, "Current user fetched"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        log.info("Logout request received");
        return ResponseEntity.ok(ApiResponse.success(
            "You have been logged out successfully", "Logout successful"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshToken() {
        return ResponseEntity.ok(ApiResponse.success(
            "Token refresh not implemented in this demo", "Token refresh"));
    }
}