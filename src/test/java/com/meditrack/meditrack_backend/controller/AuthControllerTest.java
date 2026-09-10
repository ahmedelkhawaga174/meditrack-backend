package com.meditrack.meditrack_backend.controller;

import com.meditrack.meditrack_backend.dto.LoginResponse;
import com.meditrack.meditrack_backend.dto.RegisterResponse;
import com.meditrack.meditrack_backend.service.AuthService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void shouldLoginWithCorrectCredentials() throws Exception {

        LoginResponse loginResponse = new LoginResponse(
                5L,
                "01055556666",
                com.meditrack.meditrack_backend.enums.UserRole.PATIENT,
                LocalDateTime.of(2026, 9, 3, 10, 0),
                "Login successful"
        );

        when(authService.login(any(), any(), any()))
                .thenReturn(loginResponse);

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "phone": "01055556666",
                                          "password": "pass123"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(5))
                .andExpect(jsonPath("$.phone").value("01055556666"))
                .andExpect(jsonPath("$.role").value("PATIENT"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordIsWrong() throws Exception {

        when(authService.login(any(), any(), any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "phone": "01055556666",
                                          "password": "wrong_password"
                                        }
                                        """)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message")
                        .value("Invalid phone or password"));
    }

    @Test
    void shouldReturnBadRequestWhenPhoneIsMissing() throws Exception {

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "phone": "",
                                          "password": "pass123"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.messages.phone")
                        .value("Phone is required"));
    }

    @Test
    void shouldRegisterPatientSuccessfully() throws Exception {

        RegisterResponse response = new RegisterResponse(
                20L,
                "01077778888",
                "Registration successful. OTP sent."
        );

        when(authService.registerPatient(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "phone": "01077778888",
                                          "password": "pass123",
                                          "firstName": "Mostafa",
                                          "lastName": "Ahmed"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(20))
                .andExpect(jsonPath("$.phone").value("01077778888"))
                .andExpect(jsonPath("$.message")
                        .value("Registration successful. OTP sent."));
    }

    @Test
    void shouldReturnBadRequestWhenRegistrationPhoneIsMissing()
            throws Exception {

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "phone": "",
                                          "password": "pass123",
                                          "firstName": "Mostafa",
                                          "lastName": "Ahmed"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.messages.phone")
                        .value("Phone is required"));
    }

    @Test
    void shouldReturnBadRequestWhenRegistrationPasswordIsTooShort()
            throws Exception {

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "phone": "01077778888",
                                          "password": "123",
                                          "firstName": "Mostafa",
                                          "lastName": "Ahmed"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.messages.password")
                        .value("Password must be at least 6 characters"));
    }

    @Test
    void shouldReturnBadRequestWhenRegistrationFirstNameIsMissing()
            throws Exception {

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "phone": "01077778888",
                                          "password": "pass123",
                                          "firstName": "",
                                          "lastName": "Ahmed"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.messages.firstName")
                        .value("First name is required"));
    }

    @Test
    void shouldReturnBadRequestWhenRegistrationLastNameIsMissing()
            throws Exception {

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "phone": "01077778888",
                                          "password": "pass123",
                                          "firstName": "Mostafa",
                                          "lastName": ""
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.messages.lastName")
                        .value("Last name is required"));
    }

    @Test
    void shouldVerifyOtpSuccessfully() throws Exception {

        mockMvc.perform(
                        post("/api/auth/verify-otp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "phone": "01077778888",
                                          "otp": "123456"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$")
                                .value("OTP verified successfully")
                );
    }

    @Test
    void shouldReturnBadRequestWhenOtpIsInvalidFormat()
            throws Exception {

        mockMvc.perform(
                        post("/api/auth/verify-otp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "phone": "01077778888",
                                          "otp": "12345"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"))
                .andExpect(jsonPath("$.messages.otp")
                        .value("OTP must be 6 digits"));
    }
}