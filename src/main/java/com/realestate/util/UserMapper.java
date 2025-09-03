package com.realestate.util;

import com.realestate.model.dto.UserDTO;
import com.realestate.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        // Convert User.UserRole to UserDTO.UserRole
        if (user.getRole() != null) {
            dto.setRole(UserDTO.UserRole.valueOf(user.getRole().name()));
        }
        dto.setIsEnabled(user.getIsEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        return dto;
    }
    
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        // Password will be handled separately in service layer
        user.setPassword(dto.getPassword() != null ? dto.getPassword() : "");
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        // Convert UserDTO.UserRole to User.UserRole
        if (dto.getRole() != null) {
            user.setRole(User.UserRole.valueOf(dto.getRole().name()));
        }
        user.setIsEnabled(dto.getIsEnabled() != null ? dto.getIsEnabled() : true);
        
        return user;
    }
}