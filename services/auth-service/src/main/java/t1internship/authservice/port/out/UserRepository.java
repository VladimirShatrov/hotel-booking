package t1internship.authservice.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import t1internship.authservice.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailIgnoreCase(String email);
}
