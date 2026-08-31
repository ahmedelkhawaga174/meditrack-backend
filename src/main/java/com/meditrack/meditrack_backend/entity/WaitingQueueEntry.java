package com.meditrack.meditrack_backend.entity;

import com.meditrack.meditrack_backend.enums.QueueStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "waiting_queue",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_waiting_queue_appointment",
                        columnNames = "appointment_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitingQueueEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(
            name = "appointment_id",
            nullable = false,
            unique = true
    )
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QueueStatus status;

    @Column(name = "queue_order", nullable = false)
    private Integer queueOrder;

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