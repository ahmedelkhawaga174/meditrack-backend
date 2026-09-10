package com.meditrack.meditrack_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDiagnosisRequest {

    @NotBlank(message = "Diagnosis detail is required")
    private String diagnosis;

    private String notes;
}