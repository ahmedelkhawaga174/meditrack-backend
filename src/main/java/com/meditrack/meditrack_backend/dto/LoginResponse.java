package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long userId;
    private Long patientId;
    private Long doctorId;
    private String phone;
    private UserRole role;
    private LocalDateTime lastLoginAt;
    private String message;

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
        this.doctorId = null;
        this.phone = phone;
        this.role = role;
        this.lastLoginAt = lastLoginAt;
        this.message = message;
    }
}