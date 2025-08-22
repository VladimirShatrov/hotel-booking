package t1internship.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import t1internship.authservice.config.KeyUtils;
import t1internship.authservice.domain.AccessToken;
import t1internship.authservice.domain.RefreshToken;
import t1internship.authservice.domain.Role;
import t1internship.authservice.domain.User;
import t1internship.authservice.handler.exception.TokenException;
import t1internship.authservice.port.in.JwtInPort;
import t1internship.authservice.port.out.AccessTokenRepository;
import t1internship.authservice.port.out.RefreshTokenRepository;
import t1internship.authservice.port.out.UserRepository;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService implements JwtInPort {

    private static final String TOKEN_TYPE = "token_type";

    private static final String ROLES = "role_list";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.expiration.access}")
    private long accessTokenExpiration;

    @Value("${jwt.expiration.refresh}")
    private long refreshTokenExpiration;


    public JwtService(AccessTokenRepository accessTokenRepository,
                      RefreshTokenRepository refreshTokenRepository,
                      UserRepository userRepository) throws Exception {
        this.privateKey = KeyUtils.loadPrivateKey("keys/local/private_key.pem");
        this.publicKey = KeyUtils.loadPublicKey("keys/public_key.pem");
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String generateAccessToken(final String userEmail) {
        final User savedUser = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email: " + userEmail + " не найден"));
        final Map<String, Object> claims = Map.of(
            TOKEN_TYPE, "ACCESS_TOKEN",
            ROLES, String.join(" | ", savedUser.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())));
        final String accessToken = buildToken(userEmail, claims, this.accessTokenExpiration);
        AccessToken token = accessTokenRepository.findById(userEmail)
                .orElse(AccessToken.builder()
                        .id(userEmail)
                        .accessToken(new ArrayList<>())
                        .build());
        token.getAccessToken().add(accessToken);
        accessTokenRepository.save(token);

        return accessToken;
    }

    @Override
    public String generateRefreshToken(String userEmail) {
        final Map<String, Object> claims = Map.of(TOKEN_TYPE, "REFRESH_TOKEN");
        final String refreshToken =  buildToken(userEmail,claims,this.refreshTokenExpiration);
        RefreshToken token = refreshTokenRepository.findById(userEmail)
                .orElse(RefreshToken.builder()
                        .id(userEmail)
                        .build());
        token.setRefreshToken(refreshToken);
        refreshTokenRepository.save(token);
        return refreshToken;
    }

    @Override
    public String extractUserEmail(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public boolean isTokenValid(String token, String expectedUserEmail) {
        if (token == null || expectedUserEmail == null) {
            return false;
        }

        final String userEmail = extractUserEmail(token);
        return userEmail.equals(expectedUserEmail) && !isTokenExpired(token) && !isAccessTokenWithdrawn(token, userEmail);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration()
                .before(new Date());
    }

    @Override
    public void dropAllUserTokens(String userEmail) {
        refreshTokenRepository.deleteById(userEmail);
        accessTokenRepository.deleteById(userEmail);
    }

    @Override
    public boolean isRefreshTokenWithdrawn(String refreshToken, String userEmail) {
        RefreshToken token = this.refreshTokenRepository.findById(userEmail).orElse(null);
        return token == null || !token.getRefreshToken().equals(refreshToken);
    }

    @Override
    public boolean isAccessTokenWithdrawn(String accessToken, String userEmail) {
        AccessToken token = this.accessTokenRepository.findById(userEmail).orElse(null);
        return token == null || !token.getAccessToken().contains(accessToken);
    }

    @Override
    public String refreshToken(final String refreshToken) {
        final Claims claims = extractClaims(refreshToken);
        final String userEmail = claims.getSubject();
        if (!"REFRESH_TOKEN".equals(claims.get(TOKEN_TYPE))) {
            throw new TokenException("Не верный тип токена");
        }
        if (isTokenExpired(refreshToken) || isRefreshTokenWithdrawn(refreshToken, userEmail)) {
            throw new TokenException("Истек строк хранения токена или токен отозван");
        }
        return generateAccessToken(userEmail);
    }

    private String buildToken(String userName, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(this.privateKey)
                .compact();
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (final JwtException ex) {
            throw  new RuntimeException(ex.getMessage());
        }
    }
}
