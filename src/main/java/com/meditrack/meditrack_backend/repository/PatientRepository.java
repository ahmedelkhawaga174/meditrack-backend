package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}