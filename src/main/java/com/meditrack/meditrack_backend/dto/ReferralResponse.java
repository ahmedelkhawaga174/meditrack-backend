package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferralResponse {

    private Long appointmentId;
    private Long patientId;
    private String patientName;
    private Long currentDoctorId;
    private String currentDoctorName;
    private Long referredToDoctorId;
    private String referredToDoctorName;
    private String referralDetails;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
}