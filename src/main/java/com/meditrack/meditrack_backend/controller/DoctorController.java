package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor , @RequestParam  Long departmentId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(doctor, departmentId));
    }

}
