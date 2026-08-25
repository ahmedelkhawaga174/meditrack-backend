package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.AvailabilitySlot;
import com.meditrack.meditrack_backend.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvailabilitySlotRepository
        extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByDoctorIdAndStatus(
            Long doctorId,
            SlotStatus status
    );

    Optional<AvailabilitySlot> findByIdAndStatus(
            Long id,
            SlotStatus status
    );
}