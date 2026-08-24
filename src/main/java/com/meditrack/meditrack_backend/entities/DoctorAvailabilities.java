package com.meditrack.meditrack_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_availabilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorAvailabilities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
