package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.PatientInfoResponse;
import com.meditrack.meditrack_backend.dto.UpdatePatientRequest;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.entity.Patient;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    // R1 - View Upcoming Patient Appointments
    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointments(Long patientId) {
        ensurePatientExists(patientId);

        return appointmentRepository.findUpcomingByPatientId(
                patientId,
                LocalDate.now()
        );
    }

    // R1 - View Past Patient Appointments
    @Transactional(readOnly = true)
    public List<Appointment> getPastAppointments(Long patientId) {
        ensurePatientExists(patientId);

        return appointmentRepository.findPastByPatientId(
                patientId,
                LocalDate.now()
        );
    }

    // R2 - Manage Patient Information
    @Transactional(readOnly = true)
    public PatientInfoResponse getPatientInfo(Long patientId) {

        Patient patient = getPatientEntity(patientId);

        return toPatientInfoResponse(patient);
    }

    // R2 - Manage Patient Information
    @Transactional
    public PatientInfoResponse updatePatientInfo(
            Long patientId,
            UpdatePatientRequest request
    ) {

        Patient patient = getPatientEntity(patientId);

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());

        Patient savedPatient = patientRepository.save(patient);

        return toPatientInfoResponse(savedPatient);
    }

    // R2 - Search Patient
    @Transactional(readOnly = true)
    public List<PatientInfoResponse> searchPatients(String query) {

        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query is required");
        }

        return patientRepository.searchPatients(query.trim())
                .stream()
                .map(this::toPatientInfoResponse)
                .toList();
    }

    private Patient getPatientEntity(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));
    }

    private void ensurePatientExists(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found");
        }
    }

    private PatientInfoResponse toPatientInfoResponse(Patient patient) {

        return new PatientInfoResponse(
                patient.getId(),
                patient.getUser().getId(),
                patient.getUser().getPhone(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender()
        );
    }
}