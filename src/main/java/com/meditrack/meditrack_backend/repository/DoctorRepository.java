package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    List<Doctor> findByDepartmentIdAndIsActiveTrue(UUID departmentId);

    Optional<Doctor> findByUserId(UUID userId);
}