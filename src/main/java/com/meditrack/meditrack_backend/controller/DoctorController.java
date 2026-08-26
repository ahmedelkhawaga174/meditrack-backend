package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.DoctorResponse;
import com.meditrack.meditrack_backend.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAvailableDoctors(
            @RequestParam(name = "department") Long departmentId,
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<DoctorResponse> doctors = doctorService.getAvailableDoctors(departmentId, date);
        return ResponseEntity.ok(doctors);
    }
}