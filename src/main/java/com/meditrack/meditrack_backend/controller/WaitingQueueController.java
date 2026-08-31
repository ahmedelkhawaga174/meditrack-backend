package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.QueueOrderRequest;
import com.meditrack.meditrack_backend.dto.QueueStatusRequest;
import com.meditrack.meditrack_backend.dto.WaitingQueueResponse;
import com.meditrack.meditrack_backend.service.WaitingQueueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waiting-queue")
@RequiredArgsConstructor
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;

    @GetMapping
    public ResponseEntity<List<WaitingQueueResponse>> getWaitingQueue() {

        return ResponseEntity.ok(
                waitingQueueService.getWaitingQueue()
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<WaitingQueueResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody QueueStatusRequest request
    ) {

        WaitingQueueResponse response =
                waitingQueueService.updateStatus(
                        id,
                        request.getStatus()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/order")
    public ResponseEntity<WaitingQueueResponse> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody QueueOrderRequest request
    ) {

        WaitingQueueResponse response =
                waitingQueueService.updateOrder(
                        id,
                        request.getOrder()
                );

        return ResponseEntity.ok(response);
    }
}