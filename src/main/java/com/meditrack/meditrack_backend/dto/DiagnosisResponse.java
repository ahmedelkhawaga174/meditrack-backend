package com.meditrack.meditrack_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisResponse {

    private Long appointmentId;
    private Long patientId;
    private String patientName;
    private String diagnosis;
    private String notes;
    private LocalDateTime updatedAt;
}