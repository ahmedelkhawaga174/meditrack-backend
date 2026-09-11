package com.meditrack.meditrack_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferralRequest {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Referred to Doctor ID is required")
    private Long referredToDoctorId;

    @NotBlank(message = "Reason for referral is required")
    private String reason;

    private String notes;
}