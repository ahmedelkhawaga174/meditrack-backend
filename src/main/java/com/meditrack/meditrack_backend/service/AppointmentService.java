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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            AvailabilitySlotRepository availabilitySlotRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
    }

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

    @Transactional(readOnly = true)
    public List<PendingReferralResponse> getPendingAppointmentsForDoctor(Long doctorId) {
        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndStatusOrderByCreatedAtDesc(doctorId, AppointmentStatus.PENDING);

        return appointments.stream()
                .map(apt -> PendingReferralResponse.builder()
                        .id(apt.getId())
                        .patientId(apt.getPatient().getId())
                        .patientName(apt.getPatient().getFirstName() + " " + apt.getPatient().getLastName())
                        .notes(apt.getNotes())
                        .status(apt.getStatus().name())
                        .createdAt(apt.getCreatedAt())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public MedicalHistoryResponse getPatientMedicalHistory(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        List<Appointment> appointments = appointmentRepository.findByPatientIdOrderByCreatedAtDesc(patientId);

        List<MedicalHistoryResponse.ConsultationRecordDto> history = appointments.stream()
                .map(apt -> MedicalHistoryResponse.ConsultationRecordDto.builder()
                        .appointmentId(apt.getId())
                        .doctorName(apt.getDoctor().getFirstName() + " " + apt.getDoctor().getLastName())
                        .specialization(apt.getDoctor().getSpecialization())
                        .date(apt.getCreatedAt())
                        .diagnosis(apt.getNotes() != null ? apt.getNotes() : "No formal diagnosis recorded")
                        .prescription("Standard Follow-up Prescription")
                        .notes(apt.getNotes())
                        .build())
                .toList();

        return MedicalHistoryResponse.builder()
                .patientId(patient.getId())
                .patientName(patient.getFirstName() + " " + patient.getLastName())
                .history(history)
                .build();
    }
}