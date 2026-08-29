package com.meditrack.meditrack_backend.service;


import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.exception.ResourceNotFoundException;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {
    private  final AppointmentRepository appointmentRepository;
    private  final PatientRepository patientRepository;

    public PatientService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository
    ){
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }


    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointments (Long patiendId){
        ensurePatientsExists(patiendId);
        return appointmentRepository.findUpcomingByPatientId(patiendId, LocalDate.now());
    }

    private void ensurePatientsExists(Long patiendId) {

    }

    @Transactional(readOnly = true)
    public List<Appointment> getPastAppointments(Long patientId) {   // no "static"
        ensurePatientExists(patientId);
        return appointmentRepository.findPastByPatientId(patientId, LocalDate.now());
    }
    private void ensurePatientExists(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found");
        }
    }

}

