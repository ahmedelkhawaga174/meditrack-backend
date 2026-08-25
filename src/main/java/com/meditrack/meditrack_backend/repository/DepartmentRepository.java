package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}