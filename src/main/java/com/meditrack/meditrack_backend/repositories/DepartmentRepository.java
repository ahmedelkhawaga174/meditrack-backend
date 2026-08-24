package com.meditrack.meditrack_backend.repositories;

import com.meditrack.meditrack_backend.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
