package com.meditrack.meditrack_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorResponse {

    @NotNull(message = "Doctor ID is required")
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String specialization;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    private String departmentName;

    @NotNull(message = "Available slots list cannot be null")
    @Builder.Default
    private List<SlotResponse> availableSlots = List.of();
}