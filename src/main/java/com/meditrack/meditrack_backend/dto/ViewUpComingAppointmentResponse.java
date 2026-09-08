package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.entity.AvailabilitySlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewUpComingAppointmentResponse {

    private Long patientId;
    private String patientName;
    private AvailabilitySlot slot;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
