package com.meditrack.meditrack_backend.repositories;

import com.meditrack.meditrack_backend.entities.Department;
import com.meditrack.meditrack_backend.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByDepartment(Department department);
}
