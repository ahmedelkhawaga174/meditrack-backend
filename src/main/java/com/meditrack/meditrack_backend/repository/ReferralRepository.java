package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.Referral;
import com.meditrack.meditrack_backend.enums.ReferralStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {

    Optional<Referral> findByAppointmentId(Long appointmentId);

    List<Referral> findByReferredToDoctorIdAndStatus(Long doctorId, ReferralStatus status);

    List<Referral> findByAppointmentDoctorId(Long doctorId);

    List<Referral> findByStatus(ReferralStatus status);
}