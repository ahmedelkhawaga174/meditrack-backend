package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.PrescriptionRequest;
import com.meditrack.meditrack_backend.dto.PrescriptionResponse;
import com.meditrack.meditrack_backend.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    // =====================================================
    // R3 - ISSUE PRESCRIPTION
    // =====================================================

    @PostMapping("/consultations/{consultationId}")
    public ResponseEntity<PrescriptionResponse> issuePrescription(
            @PathVariable Long consultationId,
            @Valid @RequestBody PrescriptionRequest request
    ) {

        PrescriptionResponse prescription =
                prescriptionService.issueprescription(
                        consultationId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prescription);
    }
}