package com.meditrack.meditrack_backend.dto;

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
    private AvailabilitySlotResponse slot;
    private LocalDate date;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AvailabilitySlotResponse {
         private LocalTime startTime;
         private LocalTime endTime;
    }
}
