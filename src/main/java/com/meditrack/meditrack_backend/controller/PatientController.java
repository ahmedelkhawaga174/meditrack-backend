package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.MedicalHistoryResponse;
import com.meditrack.meditrack_backend.dto.PatientInfoResponse;
import com.meditrack.meditrack_backend.dto.PatientPrescriptionResponse;
import com.meditrack.meditrack_backend.dto.PatientResponse;
import com.meditrack.meditrack_backend.dto.UpdatePatientRequest;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.service.AppointmentService;
import com.meditrack.meditrack_backend.service.PatientService;
import com.meditrack.meditrack_backend.service.PrescriptionService;
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
    private final PrescriptionService prescriptionService;


    // =====================================================
    // R2 - SEARCH PATIENT
    // =====================================================

    @GetMapping("/search")
    public ResponseEntity<List<PatientInfoResponse>> searchPatients(
            @RequestParam String q
    ) {
        return ResponseEntity.ok(
                patientService.searchPatients(q)
        );
    }


    // =====================================================
    // R2 - MANAGE PATIENT INFORMATION
    // =====================================================

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientInfoResponse> getPatientInfo(
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(
                patientService.getPatientInfo(patientId)
        );
    }


    // =====================================================
    // R2 - MANAGE PATIENT INFORMATION
    // =====================================================

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientInfoResponse> updatePatientInfo(
            @PathVariable Long patientId,
            @Valid @RequestBody UpdatePatientRequest request
    ) {
        return ResponseEntity.ok(
                patientService.updatePatientInfo(
                        patientId,
                        request
                )
        );
    }


    // =====================================================
    // R1 - VIEW UPCOMING PATIENT APPOINTMENTS
    // =====================================================

    @GetMapping("/{patientId}/appointments/date")
    public ResponseEntity<List<PatientResponse>> getUpcomingAppointments(
            @PathVariable Long patientId
    ) {

        List<Appointment> appointments =
                patientService.getUpcomingAppointments(
                        patientId
                );

        return ResponseEntity.ok(
                appointments.stream()
                        .map(this::toResponse)
                        .toList()
        );
    }


    // =====================================================
    // R1 - VIEW PAST PATIENT APPOINTMENTS
    // =====================================================

    @GetMapping("/{patientId}/appointments/past")
    public ResponseEntity<List<PatientResponse>> getPastAppointments(
            @PathVariable Long patientId
    ) {

        List<Appointment> appointments =
                patientService.getPastAppointments(
                        patientId
                );

        return ResponseEntity.ok(
                appointments.stream()
                        .map(this::toResponse)
                        .toList()
        );
    }


    // =====================================================
    // R3 - VIEW MEDICAL HISTORY
    // =====================================================

    @GetMapping("/{patientId}/medical-history")
    public ResponseEntity<MedicalHistoryResponse> getPatientMedicalHistory(
            @PathVariable Long patientId,
            @RequestParam(required = false) Long doctorId
    ) {

        patientService.validateMedicalHistoryAccess(
                patientId
        );

        MedicalHistoryResponse medicalHistory;

        if (doctorId != null) {

            // Doctor:
            // Get medical history for this patient
            // belonging to this specific doctor.
            medicalHistory =
                    appointmentService.getPatientMedicalHistory(
                            patientId,
                            doctorId
                    );

        } else {

            // Patient:
            // Get the patient's complete medical history.
            medicalHistory =
                    appointmentService.getPatientMedicalHistory(
                            patientId
                    );
        }

        return ResponseEntity.ok(
                medicalHistory
        );
    }


    // =====================================================
    // R3 - VIEW PRESCRIPTIONS
    // =====================================================

    @GetMapping("/{patientId}/prescription")
    public ResponseEntity<List<PatientPrescriptionResponse>>
    getPatientPrescriptions(
            @PathVariable Long patientId
    ) {

        return ResponseEntity.ok(
                prescriptionService.getPatientPrescriptions(
                        patientId
                )
        );
    }


    // =====================================================
    // MAP APPOINTMENT TO RESPONSE
    // =====================================================

    private PatientResponse toResponse(
            Appointment appointment
    ) {

        var doctor = appointment.getDoctor();
        var slot = appointment.getSlot();

        return new PatientResponse(
                appointment.getId(),
                doctor.getFirstName()
                        + " "
                        + doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getDepartment().getName(),
                slot.getDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                appointment.getStatus(),
                appointment.getNotes()
        );
    }
    // =====================================================
// R2 - RECEPTIONIST VIEW ALL PATIENTS
// =====================================================

    @GetMapping
    public ResponseEntity<List<PatientInfoResponse>> getAllPatients() {

        return ResponseEntity.ok(
                patientService.getAllPatientsForReceptionist()
        );
    }
}