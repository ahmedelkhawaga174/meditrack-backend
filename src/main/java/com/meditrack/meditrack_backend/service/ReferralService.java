package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.ReferralRequest;
import com.meditrack.meditrack_backend.dto.ReferralResponse;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReferralService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public ReferralResponse createReferral(ReferralRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + request.getAppointmentId()));

        Doctor targetDoctor = doctorRepository.findById(request.getReferredToDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Target Doctor not found with ID: " + request.getReferredToDoctorId()));

        String referralText = String.format("[REFERRAL to Dr. %s %s (ID: %d)] Reason: %s%s",
                targetDoctor.getFirstName(),
                targetDoctor.getLastName(),
                targetDoctor.getId(),
                request.getReason(),
                (request.getNotes() != null && !request.getNotes().isBlank()) ? " | Notes: " + request.getNotes() : ""
        );

        if (appointment.getNotes() != null && !appointment.getNotes().isBlank()) {
            appointment.setNotes(appointment.getNotes() + "\n" + referralText);
        } else {
            appointment.setNotes(referralText);
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return ReferralResponse.builder()
                .appointmentId(updatedAppointment.getId())
                .patientId(updatedAppointment.getPatient().getId())
                .patientName(updatedAppointment.getPatient().getFirstName() + " " + updatedAppointment.getPatient().getLastName())
                .currentDoctorId(updatedAppointment.getDoctor().getId())
                .currentDoctorName("Dr. " + updatedAppointment.getDoctor().getFirstName() + " " + updatedAppointment.getDoctor().getLastName())
                .referredToDoctorId(targetDoctor.getId())
                .referredToDoctorName("Dr. " + targetDoctor.getFirstName() + " " + targetDoctor.getLastName())
                .referralDetails(referralText)
                .status(updatedAppointment.getStatus())
                .createdAt(updatedAppointment.getCreatedAt())
                .build();
    }
}