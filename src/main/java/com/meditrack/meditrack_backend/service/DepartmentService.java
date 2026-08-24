package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entities.Department;
import com.meditrack.meditrack_backend.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Department createDepartment(String name) {

        departmentRepository.findByName(name).ifPresent(d -> {
            throw new RuntimeException("Department with name '" + name + "' already exists!");
        });

        Department department = Department.builder().name(name).build();

        return departmentRepository.save(department);
    }

    public List<String> getDepartments() {

        return departmentRepository.findAll().stream()
                .map(Department::getName)
                .distinct()
                .toList();
    }
}
