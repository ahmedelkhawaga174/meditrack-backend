package com.meditrack.meditrack_backend.dto;

import com.meditrack.meditrack_backend.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PatientInfoResponse {

    private Long id;
    private Long userId;
    private String phone;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
}