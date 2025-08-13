package t1internship.authservice.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "RefreshToken")
@Getter
@Setter
public class RefreshToken {

    @Id
    @Indexed
    private String id;
    private String refreshToken;
}
