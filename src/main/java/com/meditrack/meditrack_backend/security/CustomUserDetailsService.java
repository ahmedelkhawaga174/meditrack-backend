package com.meditrack.meditrack_backend.security;

import com.meditrack.meditrack_backend.entity.User;
import com.meditrack.meditrack_backend.enums.UserStatus;
import com.meditrack.meditrack_backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with phone: " + phone)
                );

        if (user.getPasswordHash() == null) {
            throw new UsernameNotFoundException("User has no credentials: " + phone);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getPhone())
                .password(user.getPasswordHash())
                .authorities(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                )
                .disabled(user.getStatus() != UserStatus.ACTIVE)
                .build();
    }
}