package com.meditrack.meditrack_backend.service;

import com.meditrack.meditrack_backend.dto.LoginRequest;
import com.meditrack.meditrack_backend.dto.LoginResponse;
import com.meditrack.meditrack_backend.entity.User;
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
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            SecurityContextRepository securityContextRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.securityContextRepository = securityContextRepository;
    }

    @Transactional
    public LoginResponse login(
            LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContext securityContext = securityContextHolderStrategy.createEmptyContext();
        securityContext.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, httpRequest, httpResponse);

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getLastLoginAt(),
                "Login successful"
        );
    }
}
