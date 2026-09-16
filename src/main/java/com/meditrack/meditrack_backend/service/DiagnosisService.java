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
    public DiagnosisResponse recordDiagnosis(
            Long consultationId,
            RecordDiagnosisRequest request
    ) {

        Appointment appointment =
                appointmentRepository.findById(consultationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Consultation/Appointment not found with ID: "
                                                + consultationId
                                )
                        );

        String existingNotes =
                appointment.getNotes();

        String diagnosis =
                request.getDiagnosis() != null
                        ? request.getDiagnosis().trim()
                        : "";

        String updatedNotes;

        if (existingNotes != null
                && !existingNotes.isBlank()) {

            if (existingNotes.startsWith("Diagnosis:")) {

                String consultationNotes = null;

                if (existingNotes.contains("| Notes:")) {

                    consultationNotes =
                            existingNotes.substring(
                                    existingNotes.indexOf("| Notes:")
                                            + "| Notes:".length()
                            ).trim();
                }

                if (consultationNotes != null
                        && !consultationNotes.isBlank()) {

                    updatedNotes =
                            "Diagnosis: "
                                    + diagnosis
                                    + " | Notes: "
                                    + consultationNotes;

                } else {

                    updatedNotes =
                            "Diagnosis: "
                                    + diagnosis;
                }

            } else {

                updatedNotes =
                        "Diagnosis: "
                                + diagnosis
                                + " | Notes: "
                                + existingNotes.trim();
            }

        } else {

            if (request.getNotes() != null
                    && !request.getNotes().isBlank()) {

                updatedNotes =
                        "Diagnosis: "
                                + diagnosis
                                + " | Notes: "
                                + request.getNotes().trim();

            } else {

                updatedNotes =
                        "Diagnosis: "
                                + diagnosis;
            }
        }

        appointment.setNotes(updatedNotes);
        appointment.setStatus(
                AppointmentStatus.COMPLETED
        );

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return buildResponse(
                savedAppointment,
                diagnosis
        );
    }


    /**
     * Update diagnosis while preserving consultation notes.
     */
    @Transactional
    public DiagnosisResponse updateDiagnosis(
            Long consultationId,
            RecordDiagnosisRequest request
    ) {

        Appointment appointment =
                appointmentRepository.findById(consultationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Consultation/Appointment not found with ID: "
                                                + consultationId
                                )
                        );

        String newDiagnosis =
                request.getDiagnosis() != null
                        ? request.getDiagnosis().trim()
                        : "";

        String existingNotes =
                appointment.getNotes();

        String consultationNotes = null;

        /*
         * Extract the existing consultation notes.
         */
        if (existingNotes != null
                && existingNotes.contains("| Notes:")) {

            consultationNotes =
                    existingNotes.substring(
                            existingNotes.indexOf("| Notes:")
                                    + "| Notes:".length()
                    ).trim();
        }

        String updatedNotes;

        if (consultationNotes != null
                && !consultationNotes.isBlank()) {

            updatedNotes =
                    "Diagnosis: "
                            + newDiagnosis
                            + " | Notes: "
                            + consultationNotes;

        } else {

            updatedNotes =
                    "Diagnosis: "
                            + newDiagnosis;
        }

        appointment.setNotes(updatedNotes);
        appointment.setStatus(
                AppointmentStatus.COMPLETED
        );

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return buildResponse(
                savedAppointment,
                newDiagnosis
        );
    }


    private DiagnosisResponse buildResponse(
            Appointment appointment,
            String diagnosis
    ) {

        return DiagnosisResponse.builder()
                .appointmentId(
                        appointment.getId()
                )
                .patientId(
                        appointment
                                .getPatient()
                                .getId()
                )
                .patientName(
                        appointment
                                .getPatient()
                                .getFirstName()
                                + " "
                                + appointment
                                .getPatient()
                                .getLastName()
                )
                .diagnosis(diagnosis)
                .notes(
                        appointment.getNotes()
                )
                .updatedAt(
                        LocalDateTime.now()
                )
                .build();
    }
}