package com.meditrack.meditrack_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferralRequest {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Target Doctor ID is required")
    private Long referredToDoctorId;

    @NotNull(message = "Referral reason is required")
    @Size(min = 5, message = "Reason must be at least 5 characters")
    private String referralReason;
}