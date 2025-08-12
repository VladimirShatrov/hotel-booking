package t1internship.authservice.port.out;

import org.springframework.data.repository.CrudRepository;
import t1internship.authservice.domain.AccessToken;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {
}
