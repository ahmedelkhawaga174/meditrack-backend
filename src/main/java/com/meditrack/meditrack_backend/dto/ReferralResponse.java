package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.ReferralStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferralResponse {

    private Long id;
    private Long appointmentId;
    private Long patientId;
    private String patientName;
    private Long referringDoctorId;
    private String referringDoctorName;
    private Long referredToDoctorId;
    private String referredToDoctorName;
    private String referralReason;
    private ReferralStatus status;
    private LocalDateTime createdAt;
}