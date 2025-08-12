package t1internship.authservice.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import t1internship.authservice.domain.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);
}
