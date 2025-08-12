package t1internship.authservice.port.out;

import org.springframework.data.repository.CrudRepository;
import t1internship.authservice.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
