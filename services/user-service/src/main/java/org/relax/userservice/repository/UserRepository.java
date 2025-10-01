package org.relax.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.relax.userservice.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsById(UUID id);

    Optional<User> findByEmail(String email);
}
