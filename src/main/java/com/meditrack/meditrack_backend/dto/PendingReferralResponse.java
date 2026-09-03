package com.meditrack.meditrack_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingReferralResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
}