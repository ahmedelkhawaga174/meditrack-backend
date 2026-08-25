package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.Department;
import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.entity.User;
import com.meditrack.meditrack_backend.repository.DepartmentRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public Doctor createDoctor(Doctor doctor) {
        if (doctor.getUser() == null || doctor.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID must be provided!");
        }
        UUID userId = doctor.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (doctor.getDepartment() == null || doctor.getDepartment().getId() == null) {
            throw new IllegalArgumentException("Department ID must be provided!");
        }
        UUID deptId = doctor.getDepartment().getId();
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + deptId));

        doctor.setUser(user);
        doctor.setDepartment(department);

        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }
}