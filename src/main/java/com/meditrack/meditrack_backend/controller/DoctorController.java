package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.DoctorResponse;
import com.meditrack.meditrack_backend.dto.PendingReferralResponse;
import com.meditrack.meditrack_backend.service.AppointmentService;
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
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAvailableDoctors(
            @RequestParam(name = "department", required = false) Long departmentId,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<DoctorResponse> doctors = doctorService.getAvailableDoctors(departmentId, date);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping("/referrals/pending")
    public ResponseEntity<List<PendingReferralResponse>> getPendingReferrals(
            @RequestParam(name = "doctorId", required = false, defaultValue = "1") Long doctorId) {

        List<PendingReferralResponse> pendingReferrals = appointmentService.getPendingAppointmentsForDoctor(doctorId);
        return ResponseEntity.ok(pendingReferrals);
    }
}