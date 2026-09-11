package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.UserRole;

import java.time.LocalDateTime;

public class LoginResponse {

    private Long userId;
    private Long patientId;
    private String phone;
    private UserRole role;
    private LocalDateTime lastLoginAt;
    private String message;

    public LoginResponse() {
    }

    // New constructor
    public LoginResponse(
            Long userId,
            Long patientId,
            String phone,
            UserRole role,
            LocalDateTime lastLoginAt,
            String message
    ) {
        this.userId = userId;
        this.patientId = patientId;
        this.phone = phone;
        this.role = role;
        this.lastLoginAt = lastLoginAt;
        this.message = message;
    }

    // Old constructor - kept for existing tests
    public LoginResponse(
            Long userId,
            String phone,
            UserRole role,
            LocalDateTime lastLoginAt,
            String message
    ) {
        this.userId = userId;
        this.patientId = null;
        this.phone = phone;
        this.role = role;
        this.lastLoginAt = lastLoginAt;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPatientId() {
        return patientId;
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