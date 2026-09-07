package com.meditrack.meditrack_backend.dto;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientResponse {
    private Long appointmentId;
    private String doctorName;
    private String specialization;
    private String DepartmentName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentStatus status;
    private String notes;
}
