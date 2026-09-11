package com.meditrack.meditrack_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {

    private Long id;
    private Long consultationId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
