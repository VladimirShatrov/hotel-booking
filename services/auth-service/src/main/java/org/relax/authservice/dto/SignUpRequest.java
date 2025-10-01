package org.relax.authservice.dto;

public record SignUpRequest(
        String email,
        String password,
        String confirmPassword
) {
}