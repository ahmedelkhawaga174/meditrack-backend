package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.LoginRequest;
import com.meditrack.meditrack_backend.dto.LoginResponse;
import com.meditrack.meditrack_backend.dto.RegisterRequest;
import com.meditrack.meditrack_backend.dto.RegisterResponse;
import com.meditrack.meditrack_backend.entity.Patient;
import com.meditrack.meditrack_backend.entity.User;
import com.meditrack.meditrack_backend.enums.UserRole;
import com.meditrack.meditrack_backend.enums.UserStatus;
import com.meditrack.meditrack_backend.repository.PatientRepository;
import com.meditrack.meditrack_backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContextRepository securityContextRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OtpService otpService;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private HttpServletResponse httpResponse;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                authenticationManager,
                userRepository,
                securityContextRepository,
                patientRepository,
                passwordEncoder,
                otpService
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldLoginWithCorrectPhoneAndPassword() {

        User user = new User();
        user.setId(5L);
        user.setUsername("patient_mostafa");
        user.setPhone("01055556666");
        user.setPasswordHash("hashed_password");
        user.setRole(UserRole.PATIENT);
        user.setStatus(UserStatus.ACTIVE);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "01055556666",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_PATIENT"))
        );

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);

        when(userRepository.findByPhone("01055556666"))
                .thenReturn(Optional.of(user));

        LoginResponse response = authService.login(
                new LoginRequest("01055556666", "pass123"),
                httpRequest,
                httpResponse
        );

        assertEquals(5L, response.getUserId());
        assertEquals("01055556666", response.getPhone());
        assertEquals(UserRole.PATIENT, response.getRole());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getLastLoginAt());
        assertNotNull(user.getLastLoginAt());

        assertEquals(
                authentication,
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(userRepository).save(user);

        verify(securityContextRepository).saveContext(
                any(SecurityContext.class),
                any(HttpServletRequest.class),
                any(HttpServletResponse.class)
        );
    }

    @Test
    void shouldFailLoginWhenPasswordIsWrong() {

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(
                BadCredentialsException.class,
                () -> authService.login(
                        new LoginRequest(
                                "01055556666",
                                "wrong_password"
                        ),
                        httpRequest,
                        httpResponse
                )
        );

        verify(userRepository, never()).save(any(User.class));

        verify(
                securityContextRepository,
                never()
        ).saveContext(any(), any(), any());
    }

    @Test
    void shouldRegisterPatientSuccessfully() {

        RegisterRequest request = new RegisterRequest(
                "01077778888",
                "pass123",
                "Mostafa",
                "Ahmed"
        );

        when(userRepository.existsByPhone("01077778888"))
                .thenReturn(false);

        when(passwordEncoder.encode("pass123"))
                .thenReturn("encoded_password");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(10L);
                    return user;
                });

        when(patientRepository.save(any(Patient.class)))
                .thenAnswer(invocation -> {
                    Patient patient = invocation.getArgument(0);
                    patient.setId(20L);
                    return patient;
                });

        RegisterResponse response =
                authService.registerPatient(request);

        assertNotNull(response);
        assertEquals(20L, response.getPatientId());
        assertEquals("01077778888", response.getPhone());
        assertEquals(
                "Registration successful. OTP sent.",
                response.getMessage()
        );

        verify(userRepository).existsByPhone("01077778888");
        verify(passwordEncoder).encode("pass123");
        verify(userRepository).save(any(User.class));
        verify(patientRepository).save(any(Patient.class));
        verify(otpService).generateAndSendOtp(any(User.class));
    }

    @Test
    void shouldRegisterPatientAsInactiveBeforeOtpVerification() {

        RegisterRequest request = new RegisterRequest(
                "01088889999",
                "pass123",
                "Ahmed",
                "Mohamed"
        );

        when(userRepository.existsByPhone("01088889999"))
                .thenReturn(false);

        when(passwordEncoder.encode("pass123"))
                .thenReturn("encoded_password");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(11L);
                    return user;
                });

        when(patientRepository.save(any(Patient.class)))
                .thenAnswer(invocation -> {
                    Patient patient = invocation.getArgument(0);
                    patient.setId(21L);
                    return patient;
                });

        authService.registerPatient(request);

        verify(userRepository).save(
                org.mockito.ArgumentMatchers.argThat(
                        user ->
                                user.getRole() == UserRole.PATIENT
                                        && user.getStatus() == UserStatus.INACTIVE
                                        && user.getPhone().equals("01088889999")
                )
        );

        verify(otpService).generateAndSendOtp(any(User.class));
    }

    @Test
    void shouldRejectRegistrationWhenPhoneAlreadyExists() {

        RegisterRequest request = new RegisterRequest(
                "01055556666",
                "pass123",
                "Mostafa",
                "Ahmed"
        );

        when(userRepository.existsByPhone("01055556666"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> authService.registerPatient(request)
        );

        verify(userRepository).existsByPhone("01055556666");
        verify(passwordEncoder, never()).encode(any(String.class));
        verify(userRepository, never()).save(any(User.class));
        verify(patientRepository, never()).save(any(Patient.class));
        verify(otpService, never()).generateAndSendOtp(any(User.class));
    }

    @Test
    void shouldVerifyOtpSuccessfully() {

        authService.verifyOtp(
                "01077778888",
                "123456"
        );

        verify(otpService).verifyOtp(
                "01077778888",
                "123456"
        );
    }
}