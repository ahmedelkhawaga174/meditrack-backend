package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.DoctorPatientDto;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorPatientService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<DoctorPatientDto> getPatientsByDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new IllegalArgumentException("Doctor not found with ID: " + doctorId);
        }

        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);

        return appointments.stream()
                .map(apt -> DoctorPatientDto.builder()
                        .appointmentId(apt.getId())
                        .patientId(apt.getPatient().getId())
                        .patientName(apt.getPatient().getFirstName() + " " + apt.getPatient().getLastName())
                        .appointmentDate(apt.getSlot().getDate())
                        .appointmentStatus(apt.getStatus() != null ? apt.getStatus().name() : "SCHEDULED")
                        .build())
                .collect(Collectors.toList());
    }
}