package com.meditrack.meditrack_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorPatientDto {

    private Long appointmentId;
    private Long patientId;
    private String patientName;
    private LocalDate appointmentDate;
    private String appointmentStatus;
}