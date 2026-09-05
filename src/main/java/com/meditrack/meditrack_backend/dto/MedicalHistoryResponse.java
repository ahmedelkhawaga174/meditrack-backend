package com.meditrack.meditrack_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryResponse {
    private Long patientId;
    private String patientName;
    private List<ConsultationRecordDto> history;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsultationRecordDto {
        private Long appointmentId;
        private String doctorName;
        private String specialization;
        private LocalDateTime date;
        private String diagnosis;
        private String prescription;
        private String notes;
    }
}