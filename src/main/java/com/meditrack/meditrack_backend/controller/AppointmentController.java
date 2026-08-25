package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.AppointmentResponse;
import com.meditrack.meditrack_backend.dto.BookAppointmentRequest;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @Valid @RequestBody BookAppointmentRequest request
    ) {

        Appointment appointment = appointmentService.bookAppointment(
                request.getPatientId(),
                request.getDoctorId(),
                request.getSlotId(),
                request.getNotes()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(appointment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointment(
            @PathVariable Long id
    ) {

        Appointment appointment = appointmentService.getAppointment(id);

        return ResponseEntity.ok(toResponse(appointment));
    }

    private AppointmentResponse toResponse(Appointment appointment) {

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getDoctor().getId(),
                appointment.getSlot().getId(),
                appointment.getStatus(),
                appointment.getCreatedAt(),
                appointment.getNotes()
        );
    }
}