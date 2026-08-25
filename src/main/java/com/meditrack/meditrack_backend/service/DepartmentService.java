package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.Department;
import com.meditrack.meditrack_backend.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Department createDepartment(Department department) {
        departmentRepository.findByName(department.getName()).ifPresent(d -> {
            throw new RuntimeException("Department with name '" + department.getName() + "' already exists!");
        });
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public List<Department> getActiveDepartments() {
        return departmentRepository.findByIsActiveTrue();
    }

    public Department getDepartmentById(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }
}