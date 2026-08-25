package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    Optional<Department> findByName(String name);
    List<Department> findByIsActiveTrue();
}