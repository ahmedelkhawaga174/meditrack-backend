package com.meditrack.meditrack_backend.repositories;

import com.meditrack.meditrack_backend.entities.Doctor;
import com.meditrack.meditrack_backend.entities.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);
}
