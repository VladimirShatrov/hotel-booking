package org.relax.authservice.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}