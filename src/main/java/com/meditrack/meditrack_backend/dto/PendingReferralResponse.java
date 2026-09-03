package com.meditrack.meditrack_backend.dto;

import java.time.LocalDateTime;

public class PendingReferralResponse {

    Long id;
    Long patientId;
    String patientName;
    String referringDoctorName;
    String reason;
    String status;
    LocalDateTime createdAt;
}
