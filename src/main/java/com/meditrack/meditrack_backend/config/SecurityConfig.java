package com.meditrack.meditrack_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // =====================================================
                        // AUTHENTICATION
                        // =====================================================

                        .requestMatchers("/api/auth/**")
                        .permitAll()


                        // =====================================================
                        // DOCTOR DISCOVERY
                        // =====================================================

                        .requestMatchers("/api/doctors/**")
                        .permitAll()


                        // =====================================================
                        // CONSULTATIONS
                        // =====================================================

                        .requestMatchers("/api/consultations/**")
                        .hasAnyRole("DOCTOR", "PATIENT")


                        // =====================================================
                        // RECEPTIONIST - PATIENTS
                        // =====================================================

                        .requestMatchers("/api/patients/search")
                        .hasRole("RECEPTIONIST")

                        .requestMatchers("/api/patients")
                        .hasRole("RECEPTIONIST")


                        // =====================================================
                        // PATIENT APIs
                        // =====================================================

                        .requestMatchers("/api/patients/**")
                        .authenticated()


                        // =====================================================
                        // RECEPTIONIST - CHECK IN
                        // =====================================================

                        .requestMatchers("/api/appointments/*/check-in")
                        .hasRole("RECEPTIONIST")


                        // =====================================================
                        // RECEPTIONIST - WAITING QUEUE
                        // =====================================================

                        .requestMatchers("/api/waiting-queue/**")
                        .hasRole("RECEPTIONIST")


                        // =====================================================
                        // APPOINTMENTS
                        // =====================================================

                        .requestMatchers("/api/appointments/**")
                        .authenticated()


                        // =====================================================
                        // REFERRALS
                        // =====================================================

                        .requestMatchers("/api/referrals/**")
                        .permitAll()


                        // =====================================================
                        // EVERYTHING ELSE
                        // =====================================================

                        .anyRequest()
                        .authenticated()
                );

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:4200")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}