package com.meditrack.meditrack_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RescheduleAppointmentRequest {

    @NotNull
    private Long newSlotId;
}