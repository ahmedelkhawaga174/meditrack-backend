package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.ConsultationRequest;
import com.meditrack.meditrack_backend.dto.ConsultationResponse;
import com.meditrack.meditrack_backend.dto.DiagnosisResponse;
import com.meditrack.meditrack_backend.dto.RecordDiagnosisRequest;
import com.meditrack.meditrack_backend.service.ConsultationService;
import com.meditrack.meditrack_backend.service.DiagnosisService;
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
    private final DiagnosisService diagnosisService;

    @PostMapping
    public ResponseEntity<ConsultationResponse> recordConsultation(@Valid @RequestBody ConsultationRequest request) {
        ConsultationResponse response = consultationService.recordConsultation(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{consultationId}/diagnoses")
    public ResponseEntity<DiagnosisResponse> recordDiagnosis(
            @PathVariable Long consultationId,
            @Valid @RequestBody RecordDiagnosisRequest request) {

        DiagnosisResponse response = diagnosisService.recordDiagnosis(consultationId, request);
        return ResponseEntity.ok(response);
    }
}