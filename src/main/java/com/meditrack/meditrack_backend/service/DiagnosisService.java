package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.RecordDiagnosisRequest;
import com.meditrack.meditrack_backend.dto.DiagnosisResponse;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiagnosisService {

    private final AppointmentRepository appointmentRepository;

    @Transactional
    public DiagnosisResponse recordDiagnosis(Long consultationId, RecordDiagnosisRequest request) {
        Appointment appointment = appointmentRepository.findById(consultationId)
                .orElseThrow(() -> new IllegalArgumentException("Consultation/Appointment not found with ID: " + consultationId));

        String updatedNotes = request.getNotes() != null && !request.getNotes().isBlank()
                ? "Diagnosis: " + request.getDiagnosis() + " | Notes: " + request.getNotes()
                : "Diagnosis: " + request.getDiagnosis();

        appointment.setNotes(updatedNotes);
        appointment.setStatus(AppointmentStatus.COMPLETED);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return DiagnosisResponse.builder()
                .appointmentId(savedAppointment.getId())
                .patientId(savedAppointment.getPatient().getId())
                .patientName(savedAppointment.getPatient().getFirstName() + " " + savedAppointment.getPatient().getLastName())
                .diagnosis(request.getDiagnosis())
                .notes(savedAppointment.getNotes())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}