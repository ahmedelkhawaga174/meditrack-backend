package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.PatientInfoResponse;
import com.meditrack.meditrack_backend.dto.UpdatePatientRequest;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.entity.Patient;
import com.meditrack.meditrack_backend.entity.User;
import com.meditrack.meditrack_backend.enums.UserRole;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;


    // =========================
    // R1 - View Upcoming Patient Appointments
    // =========================

    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointments(Long patientId) {

        validatePatientAccess(patientId);

        return appointmentRepository.findUpcomingByPatientId(
                patientId,
                LocalDate.now()
        );
    }


    // =========================
    // R1 - View Past Patient Appointments
    // =========================

    @Transactional(readOnly = true)
    public List<Appointment> getPastAppointments(Long patientId) {

        validatePatientAccess(patientId);

        return appointmentRepository.findPastByPatientId(
                patientId,
                LocalDate.now()
        );
    }


    // =========================
    // R2 - Manage Patient Information
    // =========================

    @Transactional(readOnly = true)
    public PatientInfoResponse getPatientInfo(Long patientId) {

        validatePatientAccess(patientId);

        Patient patient = getPatientEntity(patientId);

        return toPatientInfoResponse(patient);
    }


    // =========================
    // R2 - Update Patient Information
    // =========================

    @Transactional
    public PatientInfoResponse updatePatientInfo(
            Long patientId,
            UpdatePatientRequest request
    ) {

        validatePatientAccess(patientId);

        Patient patient = getPatientEntity(patientId);

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());

        Patient savedPatient = patientRepository.save(patient);

        return toPatientInfoResponse(savedPatient);
    }


    // =========================
    // R2 - Search Patient
    // =========================

    @Transactional(readOnly = true)
    public List<PatientInfoResponse> searchPatients(String query) {

        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Search query is required"
            );
        }

        return patientRepository.searchPatients(query.trim())
                .stream()
                .map(this::toPatientInfoResponse)
                .toList();
    }


    // =========================
    // PATIENT AUTHORIZATION
    // =========================
    public void validatePatientAccessForCurrentPatient(Long patientId) {
        validatePatientAccess(patientId);
    }

    private void validatePatientAccess(Long patientId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new AccessDeniedException(
                    "You must be logged in"
            );
        }

        String phone = authentication.getName();

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        /*
         * Receptionist can access patient information
         * and appointments.
         */
        if (user.getRole() == UserRole.RECEPTIONIST) {
            return;
        }

        /*
         * Patient can access only his own data.
         */
        if (user.getRole() == UserRole.PATIENT) {

            Patient currentPatient =
                    patientRepository.findByUser_Id(user.getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Patient profile not found"
                                    )
                            );

            if (!currentPatient.getId().equals(patientId)) {

                throw new AccessDeniedException(
                        "You cannot access another patient's data"
                );
            }

            return;
        }

        throw new AccessDeniedException(
                "You do not have permission to access patient data"
        );
    }
    public void validateMedicalHistoryAccess(Long patientId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new AccessDeniedException(
                    "You must be logged in"
            );
        }

        String phone = authentication.getName();

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        if (user.getRole() == UserRole.PATIENT) {

            Patient currentPatient =
                    patientRepository.findByUser_Id(user.getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Patient profile not found"
                                    )
                            );

            if (!currentPatient.getId().equals(patientId)) {
                throw new AccessDeniedException(
                        "You cannot access another patient's medical history"
                );
            }

            return;
        }

        if (user.getRole() == UserRole.DOCTOR) {
            return;
        }

        throw new AccessDeniedException(
                "You do not have permission to access medical history"
        );
    }

    // =========================
    // Helpers
    // =========================

    private Patient getPatientEntity(Long patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found"
                        )
                );
    }


    private PatientInfoResponse toPatientInfoResponse(
            Patient patient
    ) {

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