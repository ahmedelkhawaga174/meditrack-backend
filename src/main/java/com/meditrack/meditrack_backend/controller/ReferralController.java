package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.ReferralRequest;
import com.meditrack.meditrack_backend.dto.ReferralResponse;
import com.meditrack.meditrack_backend.dto.UpdateReferralStatusRequest;
import com.meditrack.meditrack_backend.service.ReferralService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referrals")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReferralController {

    private final ReferralService referralService;


    // =====================================================
    // CREATE REFERRAL
    // =====================================================

    @PostMapping
    public ResponseEntity<ReferralResponse> createReferral(
            @Valid @RequestBody ReferralRequest request
    ) {

        ReferralResponse response =
                referralService.createReferral(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =====================================================
    // GET REFERRAL DETAILS
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<ReferralResponse> getReferralDetails(
            @PathVariable Long id
    ) {

        ReferralResponse response =
                referralService.getReferralById(id);

        return ResponseEntity.ok(response);
    }


    // =====================================================
    // GET PENDING INCOMING REFERRALS
    // =====================================================

    @GetMapping("/pending")
    public ResponseEntity<List<ReferralResponse>> getPendingReferrals(
            @RequestParam Long doctorId
    ) {

        return ResponseEntity.ok(
                referralService.getPendingReferralsForDoctor(
                        doctorId
                )
        );
    }


    // =====================================================
    // GET SENT REFERRALS
    // =====================================================

    @GetMapping("/sent")
    public ResponseEntity<List<ReferralResponse>> getSentReferrals(
            @RequestParam Long doctorId
    ) {

        return ResponseEntity.ok(
                referralService.getSentReferralsForDoctor(
                        doctorId
                )
        );
    }


    // =====================================================
    // GET REFERRAL HISTORY
    // =====================================================

    @GetMapping("/history")
    public ResponseEntity<List<ReferralResponse>> getReferralHistory(
            @RequestParam Long doctorId
    ) {

        return ResponseEntity.ok(
                referralService.getReferralHistoryForDoctor(
                        doctorId
                )
        );
    }


    // =====================================================
    // UPDATE REFERRAL STATUS
    // =====================================================

    @PutMapping("/{id}/status")
    public ResponseEntity<ReferralResponse> updateReferralStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReferralStatusRequest request
    ) {

        return ResponseEntity.ok(
                referralService.updateReferralStatus(
                        id,
                        request.getStatus()
                )
        );
    }
}