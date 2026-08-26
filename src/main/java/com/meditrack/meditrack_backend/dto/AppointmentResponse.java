package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long slotId;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private String notes;
}