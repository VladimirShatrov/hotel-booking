package org.relax.authservice.dto;

import java.util.UUID;

public record UserData(
        UUID id,
        String email,
        String password,
        boolean locked,
        boolean enabled,
        boolean credentialsExpired
) {
}
