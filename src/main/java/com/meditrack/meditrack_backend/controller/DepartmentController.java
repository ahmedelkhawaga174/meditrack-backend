package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.entities.Department;
import com.meditrack.meditrack_backend.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody String department) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.createDepartment(department));
    }

    @GetMapping
    public ResponseEntity<List<String>> getDepartments() {
        return ResponseEntity.ok(departmentService.getDepartments());
    }

}
