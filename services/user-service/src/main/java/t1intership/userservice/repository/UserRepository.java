package t1intership.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t1intership.userservice.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsById(UUID id);

    Optional<User> findByEmail(String email);
}
