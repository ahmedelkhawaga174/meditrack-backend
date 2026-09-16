package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.DoctorPatientDto;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.repository.AppointmentRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorPatientService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<DoctorPatientDto> getPatientsByDoctor(Long doctorId) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new IllegalArgumentException(
                    "Doctor not found with ID: " + doctorId
            );
        }

        List<Appointment> appointments =
                appointmentRepository.findByDoctorId(doctorId);

        Map<Long, List<Appointment>> appointmentsByPatient =
                appointments.stream()
                        .collect(Collectors.groupingBy(
                                appointment ->
                                        appointment.getPatient().getId()
                        ));

        return appointmentsByPatient.values()
                .stream()
                .map(patientAppointments -> {

                    Appointment latestAppointment =
                            patientAppointments.stream()
                                    .max((a1, a2) ->
                                            a1.getSlot()
                                                    .getDate()
                                                    .compareTo(
                                                            a2.getSlot().getDate()
                                                    )
                                    )
                                    .orElseThrow();

                    var patient =
                            latestAppointment.getPatient();

                    return DoctorPatientDto.builder()
                            .patientId(patient.getId())
                            .patientName(
                                    patient.getFirstName()
                                            + " "
                                            + patient.getLastName()
                            )
                            .patientPhone(
                                    patient.getUser().getPhone()
                            )
                            .appointmentsCount(
                                    patientAppointments.size()
                            )
                            .lastAppointmentDate(
                                    latestAppointment
                                            .getSlot()
                                            .getDate()
                            )
                            .build();
                })
                .sorted((p1, p2) ->
                        p2.getLastAppointmentDate()
                                .compareTo(
                                        p1.getLastAppointmentDate()
                                )
                )
                .toList();
    }
}