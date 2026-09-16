package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.ConsultationRequest;
import com.meditrack.meditrack_backend.dto.ConsultationResponse;
import com.meditrack.meditrack_backend.dto.NoteRequest;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final AppointmentRepository appointmentRepository;

    @Transactional
    public ConsultationResponse recordConsultation(
            ConsultationRequest request
    ) {

        Appointment appointment =
                appointmentRepository.findById(
                        request.getAppointmentId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Appointment not found with ID: "
                                        + request.getAppointmentId()
                        )
                );

        /*
         * If a diagnosis already exists, preserve it.
         */
        String existingNotes = appointment.getNotes();

        String updatedNotes;

        if (existingNotes != null
                && existingNotes.startsWith("Diagnosis:")
                && existingNotes.contains("| Notes:")) {

            String diagnosis =
                    existingNotes.substring(
                            "Diagnosis:".length(),
                            existingNotes.indexOf("| Notes:")
                    ).trim();

            updatedNotes =
                    "Diagnosis: "
                            + diagnosis
                            + " | Notes: "
                            + request.getNotes();

        } else {

            updatedNotes = request.getNotes();
        }

        appointment.setNotes(updatedNotes);
        appointment.setStatus(AppointmentStatus.COMPLETED);

        Appointment updatedAppointment =
                appointmentRepository.save(appointment);

        return mapToResponse(updatedAppointment);
    }


    @Transactional
    public ConsultationResponse addNoteToConsultation(
            Long consultationId,
            NoteRequest request
    ) {

        Appointment appointment =
                appointmentRepository.findById(consultationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Appointment not found with ID: "
                                                + consultationId
                                )
                        );

        String updatedNotes =
                buildNotesWithExistingDiagnosis(
                        appointment.getNotes(),
                        request.getContent()
                );

        appointment.setNotes(updatedNotes);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return mapToResponse(savedAppointment);
    }


    /**
     * Update consultation notes while preserving diagnosis.
     */
    @Transactional
    public ConsultationResponse updateConsultationNote(
            Long consultationId,
            NoteRequest request
    ) {

        Appointment appointment =
                appointmentRepository.findById(consultationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Appointment not found with ID: "
                                                + consultationId
                                )
                        );

        String updatedNotes =
                buildNotesWithExistingDiagnosis(
                        appointment.getNotes(),
                        request.getContent()
                );

        appointment.setNotes(updatedNotes);

        Appointment updatedAppointment =
                appointmentRepository.save(appointment);

        return mapToResponse(updatedAppointment);
    }


    /**
     * Keeps the existing diagnosis and replaces only the consultation notes.
     */
    private String buildNotesWithExistingDiagnosis(
            String existingNotes,
            String newConsultationNotes
    ) {

        if (newConsultationNotes == null) {
            newConsultationNotes = "";
        }

        newConsultationNotes =
                newConsultationNotes.trim();

        if (existingNotes != null
                && existingNotes.startsWith("Diagnosis:")) {

            int notesSeparator =
                    existingNotes.indexOf("| Notes:");

            if (notesSeparator >= 0) {

                String diagnosis =
                        existingNotes.substring(
                                "Diagnosis:".length(),
                                notesSeparator
                        ).trim();

                return "Diagnosis: "
                        + diagnosis
                        + " | Notes: "
                        + newConsultationNotes;
            }

            /*
             * Diagnosis exists but there are no notes yet.
             */
            String diagnosis =
                    existingNotes.substring(
                            "Diagnosis:".length()
                    ).trim();

            return "Diagnosis: "
                    + diagnosis
                    + " | Notes: "
                    + newConsultationNotes;
        }

        /*
         * No diagnosis exists.
         */
        return newConsultationNotes;
    }


    private ConsultationResponse mapToResponse(
            Appointment appointment
    ) {

        return ConsultationResponse.builder()
                .appointmentId(appointment.getId())
                .patientId(
                        appointment.getPatient().getId()
                )
                .patientName(
                        appointment.getPatient().getFirstName()
                                + " "
                                + appointment.getPatient().getLastName()
                )
                .doctorId(
                        appointment.getDoctor().getId()
                )
                .doctorName(
                        "Dr. "
                                + appointment.getDoctor().getFirstName()
                                + " "
                                + appointment.getDoctor().getLastName()
                )
                .status(
                        appointment.getStatus()
                )
                .notes(
                        appointment.getNotes()
                )
                .createdAt(
                        appointment.getCreatedAt()
                )
                .build();
    }
}