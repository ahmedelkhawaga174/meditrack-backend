package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.QueueStatusRequest;
import com.meditrack.meditrack_backend.dto.WaitingQueueResponse;
import com.meditrack.meditrack_backend.entity.WaitingQueue;
import com.meditrack.meditrack_backend.enums.QueueStatus;
import com.meditrack.meditrack_backend.service.WaitingQueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waiting-queue")
@RequiredArgsConstructor
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;

    @PostMapping("/{appointmentId}")
    public ResponseEntity<WaitingQueueResponse> addToQueue(
            @PathVariable Long appointmentId
    ) {

        WaitingQueue waitingQueue =
                waitingQueueService.addToQueue(appointmentId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(waitingQueue));
    }

    @GetMapping
    public ResponseEntity<List<WaitingQueueResponse>> getWaitingQueue() {

        List<WaitingQueueResponse> response =
                waitingQueueService.getWaitingQueue()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<WaitingQueueResponse>> getQueueByStatus(
            @PathVariable QueueStatus status
    ) {

        List<WaitingQueueResponse> response =
                waitingQueueService
                        .getQueueByStatus(status)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{queueId}/status")
    public ResponseEntity<WaitingQueueResponse> updateQueueStatus(
            @PathVariable Long queueId,
            @Valid @RequestBody QueueStatusRequest request
    ) {

        WaitingQueue waitingQueue =
                waitingQueueService.updateQueueStatus(
                        queueId,
                        request.status()
                );

        return ResponseEntity.ok(toResponse(waitingQueue));
    }

    private WaitingQueueResponse toResponse(
            WaitingQueue waitingQueue
    ) {

        return new WaitingQueueResponse(
                waitingQueue.getId(),
                waitingQueue.getAppointment().getId(),
                waitingQueue.getAppointment().getPatient().getId(),
                waitingQueue.getAppointment().getDoctor().getId(),
                waitingQueue.getQueuePosition(),
                waitingQueue.getStatus(),
                waitingQueue.getCheckedInAt()
        );
    }
}