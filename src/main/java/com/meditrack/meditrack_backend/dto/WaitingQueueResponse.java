package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.QueueStatus;

import java.time.LocalDateTime;

public record WaitingQueueResponse(
        Long id,
        Long appointmentId,
        Long patientId,
        Long doctorId,
        Integer queuePosition,
        QueueStatus status,
        LocalDateTime checkedInAt
) {
}