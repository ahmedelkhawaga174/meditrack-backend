package com.meditrack.meditrack_backend.repositories;

import com.meditrack.meditrack_backend.entities.Appointment;
import com.meditrack.meditrack_backend.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Doctor> findByDoctor();
}
