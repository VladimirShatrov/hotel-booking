package t1internship.authservice.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}