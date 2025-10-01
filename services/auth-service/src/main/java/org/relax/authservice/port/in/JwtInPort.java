package org.relax.authservice.port.in;

public interface JwtInPort {

    String generateAccessToken(String userEmail);

    String generateRefreshToken(String userEmail);

    String extractUserEmail(String token);

    boolean isTokenValid(String token, String expectedUserEmail);

    boolean isTokenExpired(String token);

    void dropAllUserTokens(String userEmail);

    boolean isRefreshTokenWithdrawn(String refreshToken, String userEmail);

    boolean isAccessTokenWithdrawn(String accessToken, String userEmail);

    String refreshToken(String refreshToken);
}