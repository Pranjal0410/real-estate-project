package com.realestate.service;

import com.realestate.exception.DuplicateResourceException;
import com.realestate.exception.ResourceNotFoundException;
import com.realestate.exception.UnauthorizedException;
import com.realestate.model.dto.AuthRequestDTO;
import com.realestate.model.dto.AuthResponseDTO;
import com.realestate.model.dto.UserDTO;
import com.realestate.model.entity.User;
import com.realestate.repository.UserRepository;
import com.realestate.security.JwtTokenProvider;
import com.realestate.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public AuthResponseDTO authenticate(AuthRequestDTO authRequest) {
        log.info("Authenticating user: {}", authRequest.getUsernameOrEmail());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUsernameOrEmail(),
                    authRequest.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            User user = userRepository.findByUsernameOrEmail(authRequest.getUsernameOrEmail(), authRequest.getUsernameOrEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
            
            String token = jwtTokenProvider.generateToken(authentication);
            
            UserDTO userDTO = userMapper.toDTO(user);
            
            AuthResponseDTO response = AuthResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(userDTO)
                .success(true)
                .message("Authentication successful")
                .build();
            
            log.info("User authenticated successfully: {}", user.getUsername());
            return response;
            
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", authRequest.getUsernameOrEmail());
            throw new UnauthorizedException("Invalid username or password");
        }
    }
    
    public UserDTO register(UserDTO userDTO) {
        log.info("Registering new user: {}", userDTO.getUsername());
        
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        
        User user = userMapper.toEntity(userDTO);
        // Note: password encoding should be handled in the UserMapper or after conversion
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        if (user.getRole() == null) {
            user.setRole(User.UserRole.INVESTOR);
        }
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());
        
        return userMapper.toDTO(savedUser);
    }
    
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }
    
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return userMapper.toDTO(user);
    }
    
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public UserDTO update(Long id, UserDTO userDTO) {
        log.info("Updating user with id: {}", id);
        
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        if (!existingUser.getUsername().equals(userDTO.getUsername()) &&
            userRepository.existsByUsername(userDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        
        if (!existingUser.getEmail().equals(userDTO.getEmail()) &&
            userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        
        updateUserFields(existingUser, userDTO);
        existingUser.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully");
        
        return userMapper.toDTO(updatedUser);
    }
    
    public void delete(Long id) {
        log.info("Deleting user with id: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully");
    }
    
    public UserDTO changePassword(Long id, String oldPassword, String newPassword) {
        log.info("Changing password for user with id: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UnauthorizedException("Invalid old password");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        log.info("Password changed successfully");
        
        return userMapper.toDTO(updatedUser);
    }
    
    public List<UserDTO> findByRole(String role) {
        User.UserRole userRole = User.UserRole.valueOf(role.toUpperCase());
        return userRepository.findByRole(userRole).stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("No authenticated user");
        }
        
        String username = authentication.getName();
        return findByUsername(username);
    }
    
    public boolean isCurrentUserAdmin() {
        try {
            UserDTO currentUser = getCurrentUser();
            return UserDTO.UserRole.ADMIN == currentUser.getRole();
        } catch (Exception e) {
            return false;
        }
    }
    
    private void updateUserFields(User user, UserDTO dto) {
        Optional.ofNullable(dto.getUsername()).ifPresent(user::setUsername);
        Optional.ofNullable(dto.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(dto.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(dto.getPhoneNumber()).ifPresent(user::setPhoneNumber);
        if (dto.getRole() != null) {
            user.setRole(User.UserRole.valueOf(dto.getRole().name()));
        }
    }
    
    public long getUserCount() {
        return userRepository.count();
    }
    
    public List<UserDTO> searchUsers(String query) {
        // Using the custom search query from repository with Pageable.unpaged()
        return userRepository.searchUsers(null, null, query, 
            org.springframework.data.domain.Pageable.unpaged()).getContent().stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }
}