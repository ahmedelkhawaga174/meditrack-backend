package com.meditrack.meditrack_backend.repositories;

import com.meditrack.meditrack_backend.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorAvailability extends JpaRepository<com.meditrack.meditrack_backend.entities.DoctorAvailability, Long> {

    List<Doctor> findByDoctor();
}
