package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.ConsultationRequest;
import com.meditrack.meditrack_backend.dto.ConsultationResponse;
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
    public ConsultationResponse recordConsultation(ConsultationRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + request.getAppointmentId()));

        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.COMPLETED);

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return ConsultationResponse.builder()
                .appointmentId(updatedAppointment.getId())
                .patientId(updatedAppointment.getPatient().getId())
                .patientName(updatedAppointment.getPatient().getFirstName() + " " + updatedAppointment.getPatient().getLastName())
                .doctorId(updatedAppointment.getDoctor().getId())
                .doctorName("Dr. " + updatedAppointment.getDoctor().getFirstName() + " " + updatedAppointment.getDoctor().getLastName())
                .status(updatedAppointment.getStatus())
                .notes(updatedAppointment.getNotes())
                .createdAt(updatedAppointment.getCreatedAt())
                .build();
    }
}