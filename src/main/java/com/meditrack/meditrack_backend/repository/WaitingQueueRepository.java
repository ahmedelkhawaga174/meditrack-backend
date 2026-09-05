package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.WaitingQueue;
import com.meditrack.meditrack_backend.enums.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface WaitingQueueRepository extends JpaRepository<WaitingQueue, Long> {
    boolean existsByAppointmentId(Long appointmentId);
    List<WaitingQueue> findAllByOrderByQueuePositionAsc();
    List<WaitingQueue> findByStatusOrderByQueuePositionAsc(QueueStatus status);
    @Query("""
            SELECT COALESCE(MAX(w.queuePosition), 0)
            FROM WaitingQueue w
            WHERE w.appointment.slot.date = :date
            """)
    Integer findMaxQueuePositionByDate(LocalDate date);
}