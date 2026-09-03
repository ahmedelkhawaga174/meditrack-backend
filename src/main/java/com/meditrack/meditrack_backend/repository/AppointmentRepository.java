package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findUpcomingByPatientId(Long patiendId, LocalDate now);

    List<Appointment> findPastByPatientId(Long patientId, LocalDate now);

    List<Appointment> findByDoctorIdAndStatusOrderByCreatedAtDesc(Long doctorId, AppointmentStatus status);
}