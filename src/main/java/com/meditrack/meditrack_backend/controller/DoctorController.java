package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.DoctorPatientDto;
import com.meditrack.meditrack_backend.dto.DoctorResponse;
import com.meditrack.meditrack_backend.dto.PendingReferralResponse;
import com.meditrack.meditrack_backend.dto.ViewUpComingAppointmentResponse;
import com.meditrack.meditrack_backend.service.AppointmentService;
import com.meditrack.meditrack_backend.service.DoctorPatientService;
import com.meditrack.meditrack_backend.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.meditrack.meditrack_backend.dto.CreateAvailabilityRequest;
import com.meditrack.meditrack_backend.dto.SlotResponse;
import com.meditrack.meditrack_backend.service.AvailabilitySlotService;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/doctors")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DoctorPatientService doctorPatientService;
    private final AvailabilitySlotService availabilitySlotService;

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

    @GetMapping("{doctorId}/appointments/upcoming")
    public ResponseEntity<List<ViewUpComingAppointmentResponse>> viewUpComingAppointment(@PathVariable Long doctorId) {

        List<ViewUpComingAppointmentResponse> upComingAppointment = appointmentService.viewUpComingAppointment(doctorId);

        return ResponseEntity.ok(upComingAppointment);
    }

    @GetMapping("{doctorId}/patients")
    public ResponseEntity<List<DoctorPatientDto>> getDoctorPatients(@PathVariable Long doctorId) {
        List<DoctorPatientDto> patients = doctorPatientService.getPatientsByDoctor(doctorId);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("{doctorId}/appointments/today")
    public ResponseEntity<List<ViewUpComingAppointmentResponse>> viewTodaySchedule(
            @PathVariable Long doctorId
    ) {

        List<ViewUpComingAppointmentResponse> todayAppointments =
                appointmentService.viewTodaySchedule(doctorId);

        return ResponseEntity.ok(todayAppointments);
    }

    // =====================================================
// DOCTOR AVAILABILITY
// =====================================================

    @PostMapping("/{doctorId}/availability")
    public ResponseEntity<List<SlotResponse>> createAvailability(
            @PathVariable Long doctorId,
            @Valid @RequestBody CreateAvailabilityRequest request
    ) {

        List<SlotResponse> slots =
                availabilitySlotService.createAvailability(
                        doctorId,
                        request
                );

        return ResponseEntity.ok(slots);
    }


    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<List<SlotResponse>> getDoctorAvailability(
            @PathVariable Long doctorId
    ) {

        return ResponseEntity.ok(
                availabilitySlotService.getDoctorAvailability(
                        doctorId
                )
        );
    }


    @DeleteMapping("/{doctorId}/availability/{slotId}")
    public ResponseEntity<Void> deleteAvailableSlot(
            @PathVariable Long doctorId,
            @PathVariable Long slotId
    ) {

        availabilitySlotService.deleteAvailableSlot(
                doctorId,
                slotId
        );

        return ResponseEntity.noContent().build();
    }
}