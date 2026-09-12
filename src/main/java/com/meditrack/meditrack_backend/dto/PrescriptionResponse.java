package com.meditrack.meditrack_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {

    private Long prescriptionId;
    private Long consultantId;
    private Long patientId;
    private Long doctorId;
    private String medicineName;
    private String dosage;
    private String frequency;
    private String duration;
}
