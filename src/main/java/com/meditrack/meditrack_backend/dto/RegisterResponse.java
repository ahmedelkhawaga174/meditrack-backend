package com.meditrack.meditrack_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

    private Long patientId;
    private String phone;
    private String message;
}