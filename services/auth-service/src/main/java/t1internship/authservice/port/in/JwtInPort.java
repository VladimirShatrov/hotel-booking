package t1internship.authservice.port.in;

import t1internship.authservice.domain.User;

import java.time.Instant;

public interface JwtInPort {

    String generateAccessToken(String userEmail);

    String generateRefreshToken(String userEmail);

    String generateToken(User user, long expirationMillis);

    String extractUserEmail(String token);

    boolean isTokenValid(String token, String expectedUserEmail);

    boolean isTokenExpired(String token);

    void dropAllUserTokens(String userEmail);

    boolean isRefreshTokenWithdrawn(String refreshToken, String userEmail);

    boolean isAccessTokenWithdrawn(String accessToken, String userEmail);

    Instant extractExpiration(String token);

    String refreshToken(String refreshToken);
}