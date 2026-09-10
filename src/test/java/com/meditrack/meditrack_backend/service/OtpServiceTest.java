package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.entity.OtpVerification;
import com.meditrack.meditrack_backend.entity.User;
import com.meditrack.meditrack_backend.enums.UserRole;
import com.meditrack.meditrack_backend.enums.UserStatus;
import com.meditrack.meditrack_backend.repository.OtpVerificationRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private OtpVerificationRepository otpVerificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private OtpService otpService;

    @BeforeEach
    void setUp() {
        otpService = new OtpService(
                otpVerificationRepository,
                userRepository,
                passwordEncoder
        );
    }

    @Test
    void shouldGenerateAndSaveOtp() {

        User user = User.builder()
                .id(1L)
                .phone("01077778888")
                .role(UserRole.PATIENT)
                .status(UserStatus.INACTIVE)
                .build();

        when(otpVerificationRepository.findByUserPhone("01077778888"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(any(String.class)))
                .thenReturn("hashed_otp");

        otpService.generateAndSendOtp(user);

        ArgumentCaptor<OtpVerification> captor =
                ArgumentCaptor.forClass(OtpVerification.class);

        verify(otpVerificationRepository).save(captor.capture());

        OtpVerification savedOtp = captor.getValue();

        assertEquals(user, savedOtp.getUser());
        assertEquals("hashed_otp", savedOtp.getOtpHash());
        assertFalse(savedOtp.isVerified());
        assertNotNull(savedOtp.getExpiresAt());

        assertTrue(
                savedOtp.getExpiresAt().isAfter(LocalDateTime.now())
        );

        verify(passwordEncoder).encode(any(String.class));
    }

    @Test
    void shouldRegenerateExistingOtp() {

        User user = User.builder()
                .id(1L)
                .phone("01077778888")
                .role(UserRole.PATIENT)
                .status(UserStatus.INACTIVE)
                .build();

        OtpVerification existingOtp = OtpVerification.builder()
                .id(10L)
                .user(user)
                .otpHash("old_hash")
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .verified(true)
                .build();

        when(otpVerificationRepository.findByUserPhone("01077778888"))
                .thenReturn(Optional.of(existingOtp));

        when(passwordEncoder.encode(any(String.class)))
                .thenReturn("new_hash");

        otpService.generateAndSendOtp(user);

        verify(otpVerificationRepository).save(existingOtp);

        assertEquals("new_hash", existingOtp.getOtpHash());
        assertFalse(existingOtp.isVerified());
        assertTrue(existingOtp.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    @Test
    void shouldVerifyCorrectOtpAndActivateUser() {

        User user = User.builder()
                .id(1L)
                .phone("01077778888")
                .role(UserRole.PATIENT)
                .status(UserStatus.INACTIVE)
                .build();

        OtpVerification otpVerification = OtpVerification.builder()
                .id(10L)
                .user(user)
                .otpHash("hashed_otp")
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .verified(false)
                .build();

        when(userRepository.findByPhone("01077778888"))
                .thenReturn(Optional.of(user));

        when(otpVerificationRepository.findByUserPhone("01077778888"))
                .thenReturn(Optional.of(otpVerification));

        when(passwordEncoder.matches("123456", "hashed_otp"))
                .thenReturn(true);

        otpService.verifyOtp("01077778888", "123456");

        assertTrue(otpVerification.isVerified());
        assertEquals(UserStatus.ACTIVE, user.getStatus());

        verify(otpVerificationRepository).save(otpVerification);
        verify(userRepository).save(user);
    }

    @Test
    void shouldRejectIncorrectOtp() {

        User user = User.builder()
                .id(1L)
                .phone("01077778888")
                .role(UserRole.PATIENT)
                .status(UserStatus.INACTIVE)
                .build();

        OtpVerification otpVerification = OtpVerification.builder()
                .id(10L)
                .user(user)
                .otpHash("hashed_otp")
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .verified(false)
                .build();

        when(userRepository.findByPhone("01077778888"))
                .thenReturn(Optional.of(user));

        when(otpVerificationRepository.findByUserPhone("01077778888"))
                .thenReturn(Optional.of(otpVerification));

        when(passwordEncoder.matches("999999", "hashed_otp"))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> otpService.verifyOtp("01077778888", "999999")
        );

        assertEquals("Invalid phone or OTP", exception.getMessage());

        assertFalse(otpVerification.isVerified());
        assertEquals(UserStatus.INACTIVE, user.getStatus());

        verify(otpVerificationRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldRejectExpiredOtp() {

        User user = User.builder()
                .id(1L)
                .phone("01077778888")
                .role(UserRole.PATIENT)
                .status(UserStatus.INACTIVE)
                .build();

        OtpVerification otpVerification = OtpVerification.builder()
                .id(10L)
                .user(user)
                .otpHash("hashed_otp")
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .verified(false)
                .build();

        when(userRepository.findByPhone("01077778888"))
                .thenReturn(Optional.of(user));

        when(otpVerificationRepository.findByUserPhone("01077778888"))
                .thenReturn(Optional.of(otpVerification));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> otpService.verifyOtp("01077778888", "123456")
        );

        assertEquals("OTP has expired", exception.getMessage());

        assertFalse(otpVerification.isVerified());
        assertEquals(UserStatus.INACTIVE, user.getStatus());

        verify(passwordEncoder, never())
                .matches(any(String.class), any(String.class));

        verify(otpVerificationRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldRejectAlreadyVerifiedOtp() {

        User user = User.builder()
                .id(1L)
                .phone("01077778888")
                .role(UserRole.PATIENT)
                .status(UserStatus.ACTIVE)
                .build();

        OtpVerification otpVerification = OtpVerification.builder()
                .id(10L)
                .user(user)
                .otpHash("hashed_otp")
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .verified(true)
                .build();

        when(userRepository.findByPhone("01077778888"))
                .thenReturn(Optional.of(user));

        when(otpVerificationRepository.findByUserPhone("01077778888"))
                .thenReturn(Optional.of(otpVerification));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> otpService.verifyOtp("01077778888", "123456")
        );

        assertEquals(
                "OTP has already been verified",
                exception.getMessage()
        );

        verify(passwordEncoder, never())
                .matches(any(String.class), any(String.class));

        verify(otpVerificationRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldRejectUnknownPhone() {

        when(userRepository.findByPhone("01099999999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> otpService.verifyOtp("01099999999", "123456")
        );

        assertEquals(
                "Invalid phone or OTP",
                exception.getMessage()
        );

        verify(otpVerificationRepository, never())
                .findByUserPhone(any(String.class));

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldRejectWhenOtpRecordDoesNotExist() {

        User user = User.builder()
                .id(1L)
                .phone("01077778888")
                .role(UserRole.PATIENT)
                .status(UserStatus.INACTIVE)
                .build();

        when(userRepository.findByPhone("01077778888"))
                .thenReturn(Optional.of(user));

        when(otpVerificationRepository.findByUserPhone("01077778888"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> otpService.verifyOtp("01077778888", "123456")
        );

        assertEquals(
                "Invalid phone or OTP",
                exception.getMessage()
        );

        verify(userRepository, never()).save(any());
    }
}