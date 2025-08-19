package t1internship.authservice.unit;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import t1internship.authservice.domain.AccessToken;
import t1internship.authservice.domain.RefreshToken;
import t1internship.authservice.domain.Role;
import t1internship.authservice.domain.User;
import t1internship.authservice.port.out.AccessTokenRepository;
import t1internship.authservice.port.out.RefreshTokenRepository;
import t1internship.authservice.port.out.UserRepository;
import t1internship.authservice.service.JwtService;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PrivateKey privateKey;

    @Mock
    private PublicKey publicKey;

    @InjectMocks
    private JwtService jwtService;

    private User testUser;
    private AccessToken testAccessToken;
    private RefreshToken testRefreshToken;
    private final String testEmail = "test@example.com";
    private final String testToken = "test.token.value";

    @BeforeEach
    public void setUp() {
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setRoles(List.of(userRole));

        testAccessToken = new AccessToken();
        testAccessToken.setId(testEmail);
        testAccessToken.setAccessToken(new ArrayList<>(List.of("existing.access.token")));

        testRefreshToken = new RefreshToken();
        testRefreshToken.setId(testEmail);
        testRefreshToken.setRefreshToken("existing.refresh.token");
    }

    @Test
    public void generateAccessToken_ValidUser_ReturnsToken() {
        when(userRepository.findByEmailIgnoreCase(testEmail)).thenReturn(Optional.of(testUser));
        when(accessTokenRepository.findById(testEmail)).thenReturn(Optional.of(testAccessToken));
        when(accessTokenRepository.save(testAccessToken)).thenReturn(testAccessToken);

        String token = jwtService.generateAccessToken(testEmail);

        assertNotNull(token);
        verify(userRepository, times(1)).findByEmailIgnoreCase(testEmail);
        verify(accessTokenRepository, times(1)).findById(testEmail);
        verify(accessTokenRepository, times(1)).save(testAccessToken);
    }

    @Test
    public void generateAccessToken_UserNotFound_ThrowsException() {
        when(userRepository.findByEmailIgnoreCase(testEmail)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> jwtService.generateAccessToken(testEmail));
        verify(userRepository, times(1)).findByEmailIgnoreCase(testEmail);
        verifyNoInteractions(accessTokenRepository);
    }

    @Test
    public void generateRefreshToken_ValidUser_ReturnsToken() {
        when(refreshTokenRepository.findById(testEmail)).thenReturn(Optional.of(testRefreshToken));
        when(refreshTokenRepository.save(testRefreshToken)).thenReturn(testRefreshToken);

        String token = jwtService.generateRefreshToken(testEmail);

        assertNotNull(token);
        verify(refreshTokenRepository, times(1)).findById(testEmail);
        verify(refreshTokenRepository, times(1)).save(testRefreshToken);
    }

    @Test
    public void isTokenValid_ValidToken_ReturnsTrue() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(testEmail);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 600000));

        try (MockedStatic<Jwts> jwtsMock = Mockito.mockStatic(Jwts.class)) {

            JwtParserBuilder parserBuilder = mock(JwtParserBuilder.class);
            JwtParser parser = mock(JwtParser.class);
            Jws<Claims> jws = mock(Jws.class);

            jwtsMock.when(Jwts::parser).thenReturn(parserBuilder);
            when(parserBuilder.verifyWith(any(PublicKey.class))).thenReturn(parserBuilder);
            when(parserBuilder.build()).thenReturn(parser);
            when(parser.parseSignedClaims(testToken)).thenReturn(jws);
            when(jws.getPayload()).thenReturn(claims);

            when(accessTokenRepository.findById(testEmail))
                    .thenReturn(Optional.of(testAccessToken));

            boolean isValid = jwtService.isTokenValid(testToken, testEmail);
            assertTrue(isValid);
        }
    }

    @Test
    void isTokenValid_ExpiredToken_ReturnsFalse() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(testEmail);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() - 10000));

        when(publicKey.getAlgorithm()).thenReturn("RSA");
        when(Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(testToken).getPayload())
                .thenReturn(claims);

        boolean isValid = jwtService.isTokenValid(testToken, testEmail);

        assertFalse(isValid);
    }

    @Test
    void isTokenValid_WithdrawnToken_ReturnsFalse() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(testEmail);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 10000));

        when(publicKey.getAlgorithm()).thenReturn("RSA");
        when(Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(testToken).getPayload())
                .thenReturn(claims);

        when(accessTokenRepository.findById(testEmail)).thenReturn(Optional.empty());

        boolean isValid = jwtService.isTokenValid(testToken, testEmail);

        assertFalse(isValid);
    }

    @Test
    void isTokenExpired_NotExpired_ReturnsFalse() {
        Claims claims = mock(Claims.class);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 10000));

        when(publicKey.getAlgorithm()).thenReturn("RSA");
        when(Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(testToken).getPayload())
                .thenReturn(claims);

        boolean isExpired = jwtService.isTokenExpired(testToken);

        assertFalse(isExpired);
    }

    @Test
    void isTokenExpired_Expired_ReturnsTrue() {
        Claims claims = mock(Claims.class);
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() - 10000));

        when(publicKey.getAlgorithm()).thenReturn("RSA");
        when(Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(testToken).getPayload())
                .thenReturn(claims);

        boolean isExpired = jwtService.isTokenExpired(testToken);

        assertTrue(isExpired);
    }

    @Test
    void dropAllUserTokens_ValidEmail_DeletesTokens() {
        jwtService.dropAllUserTokens(testEmail);

        verify(refreshTokenRepository, times(1)).deleteById(testEmail);
        verify(accessTokenRepository, times(1)).deleteById(testEmail);
    }

    @Test
    void isRefreshTokenWithdrawn_ValidToken_ReturnsFalse() {
        when(refreshTokenRepository.findById(testEmail)).thenReturn(Optional.of(testRefreshToken));

        boolean isWithdrawn = jwtService.isRefreshTokenWithdrawn("existing.refresh.token", testEmail);

        assertFalse(isWithdrawn);
    }

    @Test
    void isRefreshTokenWithdrawn_InvalidToken_ReturnsTrue() {
        when(refreshTokenRepository.findById(testEmail)).thenReturn(Optional.of(testRefreshToken));

        boolean isWithdrawn = jwtService.isRefreshTokenWithdrawn("invalid.token", testEmail);

        assertTrue(isWithdrawn);
    }

    @Test
    void isAccessTokenWithdrawn_ValidToken_ReturnsFalse() {
        when(accessTokenRepository.findById(testEmail)).thenReturn(Optional.of(testAccessToken));

        boolean isWithdrawn = jwtService.isAccessTokenWithdrawn("existing.access.token", testEmail);

        assertFalse(isWithdrawn);
    }

    @Test
    void isAccessTokenWithdrawn_InvalidToken_ReturnsTrue() {
        when(accessTokenRepository.findById(testEmail)).thenReturn(Optional.of(testAccessToken));

        boolean isWithdrawn = jwtService.isAccessTokenWithdrawn("invalid.token", testEmail);

        assertTrue(isWithdrawn);
    }

    @Test
    void refreshToken_ValidRefreshToken_ReturnsNewAccessToken() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(testEmail);
        when(claims.get(anyString())).thenReturn("REFRESH_TOKEN");
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 10000));

        when(publicKey.getAlgorithm()).thenReturn("RSA");
        when(Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(testToken).getPayload())
                .thenReturn(claims);

        when(refreshTokenRepository.findById(testEmail)).thenReturn(Optional.of(testRefreshToken));
        when(userRepository.findByEmailIgnoreCase(testEmail)).thenReturn(Optional.of(testUser));
        when(accessTokenRepository.findById(testEmail)).thenReturn(Optional.of(testAccessToken));
        when(accessTokenRepository.save(testAccessToken)).thenReturn(testAccessToken);

        String newAccessToken = jwtService.refreshToken(testToken);

        assertNotNull(newAccessToken);
    }

    @Test
    void refreshToken_InvalidTokenType_ThrowsException() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(testEmail);
        when(claims.get(anyString())).thenReturn("INVALID_TYPE");

        when(publicKey.getAlgorithm()).thenReturn("RSA");
        when(Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(testToken).getPayload())
                .thenReturn(claims);

        assertThrows(RuntimeException.class, () -> jwtService.refreshToken(testToken));
    }

    @Test
    void refreshToken_ExpiredToken_ThrowsException() {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(testEmail);
        when(claims.get(anyString())).thenReturn("REFRESH_TOKEN");
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() - 10000));

        when(publicKey.getAlgorithm()).thenReturn("RSA");
        when(Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(testToken).getPayload())
                .thenReturn(claims);

        assertThrows(RuntimeException.class, () -> jwtService.refreshToken(testToken));
    }
}
