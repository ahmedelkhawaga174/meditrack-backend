package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.Appointment;
import com.meditrack.meditrack_backend.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsBySlotIdAndIdNotAndStatusIn(
            Long slotId,
            Long appointmentId,
            List<AppointmentStatus> statuses
    );

    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.patient.id = :patientId
          AND a.slot.date >= :now
        ORDER BY a.slot.date ASC, a.slot.startTime ASC
        """)
    List<Appointment> findUpcomingByPatientId(
            @Param("patientId") Long patientId,
            @Param("now") LocalDate now
    );

    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.patient.id = :patientId
          AND a.slot.date < :now
        ORDER BY a.slot.date DESC, a.slot.startTime DESC
        """)
    List<Appointment> findPastByPatientId(
            @Param("patientId") Long patientId,
            @Param("now") LocalDate now
    );

    List<Appointment> findByDoctorIdAndStatusOrderByCreatedAtDesc(
            Long doctorId,
            AppointmentStatus status
    );

    List<Appointment> findByPatientIdOrderByCreatedAtDesc(
            Long patientId
    );

    List<Appointment> findByDoctorId(Long doctorId);

    @Query("""
    SELECT a
    FROM Appointment a
    WHERE a.patient.id = :patientId
      AND a.doctor.id = :doctorId
    ORDER BY a.createdAt DESC
    """)
    List<Appointment> findMedicalHistoryByPatientAndDoctor(
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId
    );
}