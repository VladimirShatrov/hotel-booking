package org.relax.authservice.dto;

public record SignInRequest(
        String email,
        String password
) {
}