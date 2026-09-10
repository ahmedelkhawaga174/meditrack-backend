package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.UserRole;

import java.time.LocalDateTime;

public class LoginResponse {

    private Long userId;
    private String phone;
    private UserRole role;
    private LocalDateTime lastLoginAt;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(
            Long userId,
            String phone,
            UserRole role,
            LocalDateTime lastLoginAt,
            String message
    ) {
        this.userId = userId;
        this.phone = phone;
        this.role = role;
        this.lastLoginAt = lastLoginAt;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
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