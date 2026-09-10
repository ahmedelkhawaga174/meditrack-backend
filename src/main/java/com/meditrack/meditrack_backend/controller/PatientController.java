package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.MedicalHistoryResponse;
import com.meditrack.meditrack_backend.dto.PatientInfoResponse;
import com.meditrack.meditrack_backend.dto.PatientResponse;
import com.meditrack.meditrack_backend.dto.UpdatePatientRequest;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.service.AppointmentService;
import com.meditrack.meditrack_backend.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    // R2 - Search Patient
    @GetMapping("/search")
    public ResponseEntity<List<PatientInfoResponse>> searchPatients(
            @RequestParam String q
    ) {
        return ResponseEntity.ok(
                patientService.searchPatients(q)
        );
    }

    // R2 - Manage Patient Information
    @GetMapping("/{patientId}")
    public ResponseEntity<PatientInfoResponse> getPatientInfo(
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(
                patientService.getPatientInfo(patientId)
        );
    }

    // R2 - Manage Patient Information
    @PutMapping("/{patientId}")
    public ResponseEntity<PatientInfoResponse> updatePatientInfo(
            @PathVariable Long patientId,
            @Valid @RequestBody UpdatePatientRequest request
    ) {
        return ResponseEntity.ok(
                patientService.updatePatientInfo(patientId, request)
        );
    }

    // R1 - View Upcoming Patient Appointments
    @GetMapping("/{patientId}/appointments/date")
    public ResponseEntity<List<PatientResponse>> getUpcomingAppointments(
            @PathVariable Long patientId
    ) {
        List<Appointment> appointments =
                patientService.getUpcomingAppointments(patientId);

        return ResponseEntity.ok(
                appointments.stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    // R1 - View Past Patient Appointments
    @GetMapping("/{patientId}/appointments/past")
    public ResponseEntity<List<PatientResponse>> getPastAppointments(
            @PathVariable Long patientId
    ) {
        List<Appointment> appointments =
                patientService.getPastAppointments(patientId);

        return ResponseEntity.ok(
                appointments.stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    // Existing functionality
    @GetMapping("/{patientId}/medical-history")
    public ResponseEntity<MedicalHistoryResponse> getPatientMedicalHistory(
            @PathVariable Long patientId
    ) {
        MedicalHistoryResponse medicalHistory =
                appointmentService.getPatientMedicalHistory(patientId);

        return ResponseEntity.ok(medicalHistory);
    }

    private PatientResponse toResponse(Appointment appointment) {

        var doctor = appointment.getDoctor();
        var slot = appointment.getSlot();

        return new PatientResponse(
                appointment.getId(),
                doctor.getFirstName() + " " + doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getDepartment().getName(),
                slot.getDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                appointment.getStatus(),
                appointment.getNotes()
        );
    }
}