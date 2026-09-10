package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUserPhone(String phone);

    @Query("""
            SELECT p
            FROM Patient p
            JOIN p.user u
            WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
               OR u.phone LIKE CONCAT('%', :query, '%')
            """)
    List<Patient> searchPatients(@Param("query") String query);
}