package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entities.Department;
import com.meditrack.meditrack_backend.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Department createDepartment(String name) {

        Department department = Department.builder().name(name).build();

        return departmentRepository.save(department);
    }

}
