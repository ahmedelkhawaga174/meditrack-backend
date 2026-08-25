package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.AppointmentStatus;

import java.time.LocalDateTime;

public class AppointmentResponse {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long slotId;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private String notes;

    public AppointmentResponse() {
    }

    public AppointmentResponse(
            Long id,
            Long patientId,
            Long doctorId,
            Long slotId,
            AppointmentStatus status,
            LocalDateTime createdAt,
            String notes
    ) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.slotId = slotId;
        this.status = status;
        this.createdAt = createdAt;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getNotes() {
        return notes;
    }
}