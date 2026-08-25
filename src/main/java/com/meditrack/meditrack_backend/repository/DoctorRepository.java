package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}