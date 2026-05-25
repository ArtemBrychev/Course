package com.example.tracker.dto;

import com.example.tracker.model.User;
import com.example.tracker.model.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String email;

    private UserRole role;

    private LocalDateTime createdAt;

    public static UserResponse from(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}