package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.QueueStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitingQueueResponse {

    private Long id;

    private Long appointmentId;

    private Long patientId;
    private String patientFirstName;
    private String patientLastName;

    private Long doctorId;
    private String doctorFirstName;
    private String doctorLastName;

    private LocalDate appointmentDate;
    private LocalTime appointmentStartTime;
    private LocalTime appointmentEndTime;

    private QueueStatus status;
    private Integer queueOrder;
    private LocalDateTime checkedInAt;
}