package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.ReferralRequest;
import com.meditrack.meditrack_backend.dto.ReferralResponse;
import com.meditrack.meditrack_backend.service.ReferralService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/referrals")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReferralController {

    private final ReferralService referralService;

    @PostMapping
    public ResponseEntity<ReferralResponse> createReferral(@Valid @RequestBody ReferralRequest request) {
        ReferralResponse response = referralService.createReferral(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}