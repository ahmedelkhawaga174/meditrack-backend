package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.LoginRequest;
import com.meditrack.meditrack_backend.dto.LoginResponse;
import com.meditrack.meditrack_backend.entity.User;
import com.meditrack.meditrack_backend.enums.UserRole;
import com.meditrack.meditrack_backend.enums.UserStatus;
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
    private HttpServletRequest httpRequest;

    @Mock
    private HttpServletResponse httpResponse;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                authenticationManager,
                userRepository,
                securityContextRepository
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldLoginWithCorrectUsernameAndPassword() {

        User user = new User();
        user.setId(5L);
        user.setUsername("patient_mostafa");
        user.setPasswordHash("hashed_password");
        user.setRole(UserRole.PATIENT);
        user.setStatus(UserStatus.ACTIVE);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "patient_mostafa",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_PATIENT"))
        );

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);

        when(userRepository.findByUsername("patient_mostafa"))
                .thenReturn(Optional.of(user));

        LoginResponse response = authService.login(
                new LoginRequest("patient_mostafa", "pass123"),
                httpRequest,
                httpResponse
        );

        assertEquals(5L, response.getUserId());
        assertEquals("patient_mostafa", response.getUsername());
        assertEquals(UserRole.PATIENT, response.getRole());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getLastLoginAt());
        assertNotNull(user.getLastLoginAt());

        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());

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
                        new LoginRequest("patient_mostafa", "wrong_password"),
                        httpRequest,
                        httpResponse
                )
        );

        verify(userRepository, never()).save(any(User.class));
        verify(securityContextRepository, never()).saveContext(any(), any(), any());
    }
}
