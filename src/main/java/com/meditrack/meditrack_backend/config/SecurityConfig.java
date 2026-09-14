package com.meditrack.meditrack_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Authentication endpoints are public
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/consultations/**").permitAll()

                        // Doctor discovery is public
                        .requestMatchers("/api/doctors/**").permitAll()

                        // Patient and appointment data require login
                        .requestMatchers("/api/patients/**").authenticated()
                        // receptionist authentication
                        .requestMatchers("/api/appointments/*/check-in").hasRole("RECEPTIONIST")
                        .requestMatchers("/api/waiting-queue/**").hasRole("RECEPTIONIST")
                        .requestMatchers("/api/appointments/**")
                        .authenticated()

                        .requestMatchers("/api/patients/*/medical-history").hasAnyRole("DOCTOR", "PATIENT")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

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

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}