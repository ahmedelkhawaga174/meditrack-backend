package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.WaitingQueueEntry;
import com.meditrack.meditrack_backend.enums.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WaitingQueueRepository
        extends JpaRepository<WaitingQueueEntry, Long> {

    List<WaitingQueueEntry> findAllByOrderByQueueOrderAsc();

    Optional<WaitingQueueEntry> findByAppointmentId(Long appointmentId);

    List<WaitingQueueEntry> findByStatusOrderByQueueOrderAsc(
            QueueStatus status
    );

    List<WaitingQueueEntry> findByAppointmentDoctorIdAndStatusOrderByQueueOrderAsc(
            Long doctorId,
            QueueStatus status
    );

    Optional<WaitingQueueEntry> findTopByOrderByQueueOrderDesc();
}