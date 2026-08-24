package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entities.Department;
import com.meditrack.meditrack_backend.entities.Doctor;
import com.meditrack.meditrack_backend.repositories.DepartmentRepository;
import com.meditrack.meditrack_backend.repositories.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public Doctor createDoctor(Doctor doctor, Long departmentId) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));

        Doctor newDoctor = Doctor.builder()
                .name(doctor.getName())
                .department(department).build();

        return doctorRepository.save(newDoctor);

    }

}
