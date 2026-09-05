package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.QueueStatus;
import jakarta.validation.constraints.NotNull;

public record QueueStatusRequest(

        @NotNull
        QueueStatus status

) {
}