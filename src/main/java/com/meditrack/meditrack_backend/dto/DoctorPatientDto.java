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

    private Long patientId;

    private String patientName;

    private String patientPhone;

    private long appointmentsCount;

    private LocalDate lastAppointmentDate;
}