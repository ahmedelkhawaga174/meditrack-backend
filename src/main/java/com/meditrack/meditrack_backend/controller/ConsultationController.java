package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.ConsultationRequest;
import com.meditrack.meditrack_backend.dto.ConsultationResponse;
import com.meditrack.meditrack_backend.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping
    public ResponseEntity<ConsultationResponse> recordConsultation(@Valid @RequestBody ConsultationRequest request) {
        ConsultationResponse response = consultationService.recordConsultation(request);
        return ResponseEntity.ok(response);
    }
}