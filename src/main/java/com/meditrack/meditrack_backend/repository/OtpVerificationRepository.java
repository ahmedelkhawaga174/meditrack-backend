package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findByUserPhone(String phone);
}