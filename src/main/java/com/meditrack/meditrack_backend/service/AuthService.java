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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SecurityContextRepository securityContextRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            SecurityContextRepository securityContextRepository,
            PatientRepository patientRepository,
            PasswordEncoder passwordEncoder,
            OtpService otpService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.securityContextRepository = securityContextRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
    }

    @Transactional
    public LoginResponse login(
            LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.getPhone(),
                        request.getPassword()
                )
        );

        SecurityContext securityContext =
                securityContextHolderStrategy.createEmptyContext();

        securityContext.setAuthentication(authentication);

        securityContextHolderStrategy.setContext(securityContext);

        securityContextRepository.saveContext(
                securityContext,
                httpRequest,
                httpResponse
        );

        User user = userRepository.findByPhone(authentication.getName())
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid phone or password"
                        )
                );

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return new LoginResponse(
                user.getId(),
                user.getPhone(),
                user.getRole(),
                user.getLastLoginAt(),
                "Login successful"
        );
    }

    @Transactional
    public RegisterResponse registerPatient(RegisterRequest request) {

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException(
                    "Phone number is already registered"
            );
        }

        User user = User.builder()
                .phone(request.getPhone())
                .passwordHash(
                        passwordEncoder.encode(request.getPassword())
                )
                .role(UserRole.PATIENT)
                .status(UserStatus.INACTIVE)
                .build();

        userRepository.save(user);

        Patient patient = Patient.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        patientRepository.save(patient);

        otpService.generateAndSendOtp(user);

        return new RegisterResponse(
                patient.getId(),
                user.getPhone(),
                "Registration successful. OTP sent."
        );
    }

    @Transactional
    public void verifyOtp(String phone, String otp) {
        otpService.verifyOtp(phone, otp);
    }
}