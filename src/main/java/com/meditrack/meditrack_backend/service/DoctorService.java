package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.Department;
import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.entity.User;
import com.meditrack.meditrack_backend.repository.DepartmentRepository;
import com.meditrack.meditrack_backend.repository.DoctorRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public Doctor createDoctor(Doctor doctor, UUID userId, UUID departmentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));

        doctorRepository.findByUserId(userId).ifPresent(d -> {
            throw new RuntimeException("User is already linked to another doctor profile!");
        });

        doctor.setUser(user);
        doctor.setDepartment(department);
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> getDoctorsByDepartment(UUID departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new RuntimeException("Department not found with id: " + departmentId);
        }
        return doctorRepository.findByDepartmentIdAndIsActiveTrue(departmentId);
    }

    public Doctor getDoctorById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }
}