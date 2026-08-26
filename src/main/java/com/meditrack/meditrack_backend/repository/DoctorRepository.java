package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.Doctor;
import com.meditrack.meditrack_backend.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findDistinctByDepartmentIdAndAvailabilitySlotsDateAndAvailabilitySlotsStatus(
            Long departmentId,
            LocalDate date,
            SlotStatus status
    );
}