package com.meditrack.meditrack_backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueOrderRequest {

    @NotNull(message = "Queue order is required")
    @Positive(message = "Queue order must be greater than 0")
    private Integer order;
}