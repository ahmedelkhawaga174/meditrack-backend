package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.UserRole;

import java.time.LocalDateTime;

public class LoginResponse {

    private Long userId;
    private String username;
    private UserRole role;
    private LocalDateTime lastLoginAt;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(
            Long userId,
            String username,
            UserRole role,
            LocalDateTime lastLoginAt,
            String message
    ) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.lastLoginAt = lastLoginAt;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public String getMessage() {
        return message;
    }
}
