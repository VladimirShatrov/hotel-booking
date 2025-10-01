package org.relax.authservice.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "AccessToken")
@Getter
@Setter
public class AccessToken implements Serializable {

    @Id
    @Indexed
    private String id;
    private List<String> accessToken;
}
