package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.QueueStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueStatusRequest {

    @NotNull(message = "Queue status is required")
    private QueueStatus status;
}