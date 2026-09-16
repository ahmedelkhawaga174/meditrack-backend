package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.MedicalHistoryResponse;
import com.meditrack.meditrack_backend.dto.PendingReferralResponse;
import com.meditrack.meditrack_backend.dto.PrescriptionResponse;
import com.meditrack.meditrack_backend.dto.ViewUpComingAppointmentResponse;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.entity.AvailabilitySlot;
import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.entity.Patient;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import com.meditrack.meditrack_backend.enums.SlotStatus;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.exception.SlotAlreadyBookedException;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.AvailabilitySlotRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final PrescriptionService prescriptionService;


    // =====================================================
    // GET ALL APPOINTMENTS
    // =====================================================

    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }


    // =====================================================
    // BOOK APPOINTMENT
    // =====================================================

    @Transactional
    public Appointment bookAppointment(
            Long patientId,
            Long doctorId,
            Long slotId,
            String notes
    ) {

        Patient patient =
                patientRepository.findById(patientId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient not found"
                                ));

        Doctor doctor =
                doctorRepository.findById(doctorId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found"
                                ));

        AvailabilitySlot slot =
                availabilitySlotRepository
                        .findByIdAndStatus(
                                slotId,
                                SlotStatus.AVAILABLE
                        )
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


    // =====================================================
    // GET APPOINTMENT
    // =====================================================

    @Transactional(readOnly = true)
    public Appointment getAppointment(Long appointmentId) {

        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        ));
    }


    // =====================================================
    // CHECK-IN PATIENT
    // =====================================================

    @Transactional
    public Appointment checkInPatient(Long appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found"
                                ));

        if (appointment.getStatus()
                != AppointmentStatus.CONFIRMED) {

            throw new IllegalStateException(
                    "Only confirmed appointments can be checked in"
            );
        }

        appointment.setStatus(
                AppointmentStatus.CHECKED_IN
        );

        return appointmentRepository.save(appointment);
    }


    // =====================================================
    // MEDICAL HISTORY
    // =====================================================

    @Transactional(readOnly = true)
    public MedicalHistoryResponse getPatientMedicalHistory(
            Long patientId,
            Long doctorId
    ) {
        Patient patient =
                patientRepository.findById(patientId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient not found with id: "
                                                + patientId
                                ));

        List<Appointment> appointments =
                appointmentRepository.findMedicalHistoryByPatientAndDoctor(
                        patientId,
                        doctorId
                );

        List<MedicalHistoryResponse.ConsultationRecordDto> history =
                appointments.stream()
                        .map(this::toMedicalHistoryRecord)
                        .toList();

        return MedicalHistoryResponse.builder()
                .patientId(patient.getId())
                .patientName(
                        patient.getFirstName()
                                + " "
                                + patient.getLastName()
                )
                .history(history)
                .build();
    }

    @Transactional(readOnly = true)
    public MedicalHistoryResponse getPatientMedicalHistory(
            Long patientId
    ) {
        Patient patient =
                patientRepository.findById(patientId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient not found with id: "
                                                + patientId
                                ));

        List<Appointment> appointments =
                appointmentRepository.findByPatientIdOrderByCreatedAtDesc(
                        patientId
                );

        List<MedicalHistoryResponse.ConsultationRecordDto> history =
                appointments.stream()
                        .map(this::toMedicalHistoryRecord)
                        .toList();

        return MedicalHistoryResponse.builder()
                .patientId(patient.getId())
                .patientName(
                        patient.getFirstName()
                                + " "
                                + patient.getLastName()
                )
                .history(history)
                .build();
    }


    private MedicalHistoryResponse.ConsultationRecordDto
    toMedicalHistoryRecord(Appointment appointment) {

        String appointmentNotes =
                appointment.getNotes();

        String diagnosis =
                extractDiagnosis(appointmentNotes);

        String notes =
                extractNotes(appointmentNotes);

        List<PrescriptionResponse> prescriptions =
                prescriptionService
                        .getPrescriptionsByConsultation(
                                appointment.getId()
                        );

        String prescription =
                formatPrescriptions(prescriptions);

        return MedicalHistoryResponse
                .ConsultationRecordDto
                .builder()
                .appointmentId(appointment.getId())
                .doctorName(
                        appointment.getDoctor().getFirstName()
                                + " "
                                + appointment.getDoctor().getLastName()
                )
                .specialization(
                        appointment.getDoctor().getSpecialization()
                )
                .date(appointment.getCreatedAt())
                .diagnosis(diagnosis)
                .prescription(prescription)
                .notes(notes)
                .build();
    }


    private String extractDiagnosis(String notes) {

        if (notes == null || notes.isBlank()) {
            return "No formal diagnosis recorded";
        }

        String prefix = "Diagnosis:";

        if (!notes.contains(prefix)) {
            return "No formal diagnosis recorded";
        }

        String diagnosis =
                notes.substring(
                        notes.indexOf(prefix) + prefix.length()
                );

        if (diagnosis.contains("| Notes:")) {
            diagnosis =
                    diagnosis.substring(
                            0,
                            diagnosis.indexOf("| Notes:")
                    );
        }

        diagnosis = diagnosis.trim();

        return diagnosis.isBlank()
                ? "No formal diagnosis recorded"
                : diagnosis;
    }


    private String extractNotes(String notes) {

        if (notes == null || notes.isBlank()) {
            return null;
        }

        if (notes.contains("| Notes:")) {

            String consultationNotes =
                    notes.substring(
                            notes.indexOf("| Notes:")
                                    + "| Notes:".length()
                    ).trim();

            return consultationNotes.isBlank()
                    ? null
                    : consultationNotes;
        }

        /*
         * If the appointment contains normal consultation notes
         * without a diagnosis, keep them as notes.
         */
        if (!notes.startsWith("Diagnosis:")) {
            return notes;
        }

        return null;
    }


    private String formatPrescriptions(
            List<PrescriptionResponse> prescriptions
    ) {

        if (prescriptions == null ||
                prescriptions.isEmpty()) {

            return "No prescription recorded";
        }

        return prescriptions.stream()
                .map(prescription ->
                        prescription.getMedicineName()
                                + " - "
                                + prescription.getDosage()
                                + " - "
                                + prescription.getFrequency()
                                + " - "
                                + prescription.getDuration()
                )
                .reduce(
                        (first, second) ->
                                first + "\n" + second
                )
                .orElse("No prescription recorded");
    }


    // =====================================================
    // PENDING APPOINTMENTS
    // =====================================================

    @Transactional(readOnly = true)
    public List<PendingReferralResponse>
    getPendingAppointmentsForDoctor(Long doctorId) {

        List<Appointment> appointments =
                appointmentRepository
                        .findByDoctorIdAndStatusOrderByCreatedAtDesc(
                                doctorId,
                                AppointmentStatus.PENDING
                        );

        return appointments.stream()
                .map(apt ->
                        PendingReferralResponse.builder()
                                .id(apt.getId())
                                .patientId(
                                        apt.getPatient().getId()
                                )
                                .patientName(
                                        apt.getPatient().getFirstName()
                                                + " "
                                                + apt.getPatient().getLastName()
                                )
                                .notes(apt.getNotes())
                                .status(
                                        apt.getStatus().name()
                                )
                                .createdAt(
                                        apt.getCreatedAt()
                                )
                                .build()
                )
                .toList();
    }


    // =====================================================
    // CANCEL APPOINTMENT
    // =====================================================

    @Transactional
    public Appointment cancelAppointment(
            Long appointmentId
    ) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found"
                                ));

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Appointment is already cancelled"
            );
        }

        appointment.setStatus(
                AppointmentStatus.CANCELLED
        );

        AvailabilitySlot slot =
                appointment.getSlot();

        slot.setStatus(SlotStatus.AVAILABLE);

        availabilitySlotRepository.save(slot);

        return appointmentRepository.save(appointment);
    }


    // =====================================================
    // RESCHEDULE APPOINTMENT
    // =====================================================

    @Transactional
    public Appointment rescheduleAppointment(
            Long appointmentId,
            Long newSlotId
    ) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found"
                                ));

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED
                || appointment.getStatus()
                == AppointmentStatus.COMPLETED
                || appointment.getStatus()
                == AppointmentStatus.NO_SHOW
                || appointment.getStatus()
                == AppointmentStatus.CHECKED_IN) {

            throw new IllegalStateException(
                    "This appointment cannot be rescheduled"
            );
        }

        AvailabilitySlot newSlot =
                availabilitySlotRepository
                        .findById(newSlotId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Slot not found"
                                ));

        if (newSlot.getStatus()
                != SlotStatus.AVAILABLE) {

            throw new SlotAlreadyBookedException(
                    "The new slot is already booked"
            );
        }

        if (appointmentRepository
                .existsBySlotIdAndIdNotAndStatusIn(
                        newSlotId,
                        appointmentId,
                        List.of(
                                AppointmentStatus.PENDING,
                                AppointmentStatus.CONFIRMED,
                                AppointmentStatus.CHECKED_IN
                        )
                )) {

            throw new SlotAlreadyBookedException(
                    "The new slot is already booked by another appointment"
            );
        }

        if (!newSlot.getDoctor().getId().equals(
                appointment.getDoctor().getId()
        )) {

            throw new IllegalArgumentException(
                    "The new slot does not belong to this doctor"
            );
        }

        AvailabilitySlot oldSlot =
                appointment.getSlot();

        oldSlot.setStatus(SlotStatus.AVAILABLE);
        newSlot.setStatus(SlotStatus.BOOKED);

        appointment.setSlot(newSlot);

        availabilitySlotRepository.save(oldSlot);
        availabilitySlotRepository.save(newSlot);

        return appointmentRepository.save(appointment);
    }


    // =====================================================
    // DOCTOR UPCOMING APPOINTMENTS
    // =====================================================

    public List<ViewUpComingAppointmentResponse>
    viewUpComingAppointment(Long doctorId) {

        return appointmentRepository
                .findByDoctorId(doctorId)
                .stream()
                .filter(apt ->
                        apt.getStatus()
                                == AppointmentStatus.PENDING
                                || apt.getStatus()
                                == AppointmentStatus.CONFIRMED
                                || apt.getStatus()
                                == AppointmentStatus.CHECKED_IN
                )
                .filter(apt ->
                        apt.getSlot().getDate()
                                .isEqual(LocalDate.now())
                                || apt.getSlot().getDate()
                                .isAfter(LocalDate.now())
                )
                .map(apt ->
                        ViewUpComingAppointmentResponse
                                .builder()
                                .appointmentId(apt.getId())
                                .patientName(
                                        apt.getPatient().getFirstName()
                                                + " "
                                                + apt.getPatient().getLastName()
                                )
                                .patientId(
                                        apt.getPatient().getId()
                                )
                                .slot(
                                        ViewUpComingAppointmentResponse
                                                .AvailabilitySlotResponse
                                                .builder()
                                                .startTime(
                                                        apt.getSlot()
                                                                .getStartTime()
                                                )
                                                .endTime(
                                                        apt.getSlot()
                                                                .getEndTime()
                                                )
                                                .build()
                                )
                                .date(
                                        apt.getSlot().getDate()
                                )
                                .build()
                )
                .toList();
    }


    // =====================================================
    // DOCTOR TODAY SCHEDULE
    // =====================================================

    @Transactional(readOnly = true)
    public List<ViewUpComingAppointmentResponse>
    viewTodaySchedule(Long doctorId) {

        LocalDate today =
                LocalDate.now();

        return appointmentRepository
                .findByDoctorId(doctorId)
                .stream()
                .filter(apt ->
                        apt.getStatus()
                                == AppointmentStatus.PENDING
                                || apt.getStatus()
                                == AppointmentStatus.CONFIRMED
                                || apt.getStatus()
                                == AppointmentStatus.CHECKED_IN
                )
                .filter(apt ->
                        apt.getSlot() != null
                                && today.equals(
                                apt.getSlot().getDate()
                        )
                )
                .map(apt ->
                        ViewUpComingAppointmentResponse
                                .builder()
                                .appointmentId(apt.getId())
                                .patientName(
                                        apt.getPatient().getFirstName()
                                                + " "
                                                + apt.getPatient().getLastName()
                                )
                                .patientId(
                                        apt.getPatient().getId()
                                )
                                .slot(
                                        ViewUpComingAppointmentResponse
                                                .AvailabilitySlotResponse
                                                .builder()
                                                .startTime(
                                                        apt.getSlot()
                                                                .getStartTime()
                                                )
                                                .endTime(
                                                        apt.getSlot()
                                                                .getEndTime()
                                                )
                                                .build()
                                )
                                .date(
                                        apt.getSlot().getDate()
                                )
                                .build()
                )
                .toList();
    }
}