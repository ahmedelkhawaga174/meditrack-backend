package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.Department;
import com.meditrack.meditrack_backend.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<String> getDepartmentNames() {
        return departmentRepository.findByIsActiveTrue().stream()
                .map(Department::getName)
                .distinct()
                .toList();
    }

    public Department createDepartment(Department department) {
        departmentRepository.findByName(department.getName()).ifPresent(d -> {
            throw new RuntimeException("Department with name '" + department.getName() + "' already exists!");
        });
        return departmentRepository.save(department);
    }
}