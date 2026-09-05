package com.meditrack.meditrack_backend.entity;

import com.meditrack.meditrack_backend.enums.QueueStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_queue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitingQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(nullable = false)
    private Integer queuePosition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QueueStatus status;

    @Column(name = "checked_in_at", nullable = false)
    private LocalDateTime checkedInAt;

    @PrePersist
    protected void onCreate() {
        checkedInAt = LocalDateTime.now();

        if (status == null) {
            status = QueueStatus.WAITING;
        }
    }
}