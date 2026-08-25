package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.entity.User;
import com.meditrack.meditrack_backend.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByUsername(String username);

    List<User> findByRole(UserRole role);
}