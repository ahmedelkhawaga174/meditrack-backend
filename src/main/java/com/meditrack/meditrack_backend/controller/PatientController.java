package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.PatientResponse;
import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    @GetMapping("/{patientId}/appointments/past")
    public ResponseEntity<List<PatientResponse>> getPastAppointments(
            @PathVariable Long patientId
    ){
        List<Appointment> appointments = patientService.getPastAppointments(patientId);
        return ResponseEntity.ok(appointments.stream().map(this::toResponse).toList());
    }

    private PatientResponse toResponse(Appointment appointment) {
        var doctor = appointment.getDoctor();
        var slot = appointment.getSlot();
        return new PatientResponse(
                appointment.getId(),
                doctor.getFirstName() + " " + doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getDepartment().getName(),
                slot.getDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                appointment.getStatus(),
                appointment.getNotes()
        );
    }





}
