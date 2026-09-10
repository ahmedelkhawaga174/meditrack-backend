package com.meditrack.meditrack_backend.dto;

public class RegisterResponse {

    private Long patientId;
    private String phone;
    private String message;

    public RegisterResponse() {
    }

    public RegisterResponse(Long patientId, String phone, String message) {
        this.patientId = patientId;
        this.phone = phone;
        this.message = message;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPhone() {
        return phone;
    }

    public String getMessage() {
        return message;
    }
}