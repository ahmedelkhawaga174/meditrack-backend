package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.*;
import com.meditrack.meditrack_backend.service.ConsultationService;
import com.meditrack.meditrack_backend.service.DiagnosisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{consultationId}/notes")
    public ResponseEntity<ConsultationResponse> createNote(
            @PathVariable Long consultationId,
            @Valid @RequestBody NoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultationService.addNoteToConsultation(consultationId, request));
    }

    @PutMapping("/{consultationId}/notes")
    public ResponseEntity<ConsultationResponse> updateNote(
            @PathVariable Long consultationId,
            @Valid @RequestBody NoteRequest request) {
        return ResponseEntity.ok(consultationService.updateConsultationNote(consultationId, request));
    }
}