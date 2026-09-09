package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.Appointment;
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

    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointments(Long patientId) {
        ensurePatientExists(patientId);

        return appointmentRepository.findUpcomingByPatientId(
                patientId,
                LocalDate.now()
        );
    }

    @Transactional(readOnly = true)
    public List<Appointment> getPastAppointments(Long patientId) {
        ensurePatientExists(patientId);

        return appointmentRepository.findPastByPatientId(
                patientId,
                LocalDate.now()
        );
    }

    private void ensurePatientExists(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found");
        }
    }
}