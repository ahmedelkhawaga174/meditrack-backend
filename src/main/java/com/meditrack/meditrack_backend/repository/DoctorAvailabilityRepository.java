package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.AvailabilitySlot;
import com.meditrack.meditrack_backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);
}
