package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.MedicalHistoryResponse;
import com.meditrack.meditrack_backend.dto.PendingReferralResponse;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.entity.AvailabilitySlot;
import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.entity.Patient;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import com.meditrack.meditrack_backend.enums.SlotStatus;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.AvailabilitySlotRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Transactional
    public Appointment bookAppointment(
            Long patientId,
            Long doctorId,
            Long slotId,
            String notes
    ) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));

        AvailabilitySlot slot = availabilitySlotRepository
                .findByIdAndStatus(slotId, SlotStatus.AVAILABLE)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Available slot not found"
                        ));

        if (!slot.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException(
                    "The selected slot does not belong to this doctor"
            );
        }

        slot.setStatus(SlotStatus.BOOKED);
        availabilitySlotRepository.save(slot);

        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setSlot(slot);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setNotes(notes);

        return appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public Appointment getAppointment(Long appointmentId) {

        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        ));
    }
    @Transactional
    public Appointment checkInPatient(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        )
                );

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Only confirmed appointments can be checked in"
            );
        }

        appointment.setStatus(AppointmentStatus.CHECKED_IN);

        return appointmentRepository.save(appointment);
    }
}