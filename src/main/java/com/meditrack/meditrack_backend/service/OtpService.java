package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.OtpVerification;
import com.meditrack.meditrack_backend.entity.User;
import com.meditrack.meditrack_backend.enums.UserStatus;
import com.meditrack.meditrack_backend.repository.OtpVerificationRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private static final int OTP_EXPIRATION_MINUTES = 5;

    private final OtpVerificationRepository otpVerificationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public void generateAndSendOtp(User user) {

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        OtpVerification otpVerification =
                otpVerificationRepository.findByUserPhone(user.getPhone())
                        .orElse(
                                OtpVerification.builder()
                                        .user(user)
                                        .build()
                        );

        otpVerification.setOtpHash(
                passwordEncoder.encode(otp)
        );

        otpVerification.setExpiresAt(
                LocalDateTime.now()
                        .plusMinutes(OTP_EXPIRATION_MINUTES)
        );

        otpVerification.setVerified(false);

        otpVerificationRepository.save(otpVerification);

        // Development only.
        System.out.println(
                "========================================"
        );
        System.out.println(
                "MEDiTRACK OTP"
        );
        System.out.println(
                "Phone: " + user.getPhone()
        );
        System.out.println(
                "OTP: " + otp
        );
        System.out.println(
                "Expires in: " + OTP_EXPIRATION_MINUTES + " minutes"
        );
        System.out.println(
                "========================================"
        );
    }

    @Transactional
    public void verifyOtp(String phone, String otp) {

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid phone or OTP"
                        )
                );

        OtpVerification otpVerification =
                otpVerificationRepository.findByUserPhone(phone)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid phone or OTP"
                                )
                        );

        if (otpVerification.isVerified()) {
            throw new IllegalArgumentException(
                    "OTP has already been verified"
            );
        }

        if (LocalDateTime.now().isAfter(
                otpVerification.getExpiresAt()
        )) {
            throw new IllegalArgumentException(
                    "OTP has expired"
            );
        }

        if (!passwordEncoder.matches(
                otp,
                otpVerification.getOtpHash()
        )) {
            throw new IllegalArgumentException(
                    "Invalid phone or OTP"
            );
        }

        otpVerification.setVerified(true);
        otpVerificationRepository.save(otpVerification);

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}