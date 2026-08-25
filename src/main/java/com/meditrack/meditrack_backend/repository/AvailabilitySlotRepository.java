package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.AvailabilitySlot;
import com.meditrack.meditrack_backend.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, UUID> {

    List<AvailabilitySlot> findByDoctorIdAndDateAndStatus(UUID doctorId, LocalDate date, SlotStatus status);

    List<AvailabilitySlot> findByDoctorIdAndDate(UUID doctorId, LocalDate date);
}