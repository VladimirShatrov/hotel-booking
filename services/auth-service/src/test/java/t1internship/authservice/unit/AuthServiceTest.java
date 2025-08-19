package t1internship.authservice.unit;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import t1internship.authservice.adapter.out.kafka.UserKafkaProducer;
import t1internship.authservice.domain.Role;
import t1internship.authservice.domain.User;
import t1internship.authservice.dto.AuthResponse;
import t1internship.authservice.dto.RefreshTokenRequest;
import t1internship.authservice.dto.SignInRequest;
import t1internship.authservice.dto.SignUpRequest;
import t1internship.authservice.mapper.UserMapper;
import t1internship.authservice.port.in.JwtInPort;
import t1internship.authservice.port.out.RoleRepository;
import t1internship.authservice.port.out.UserRepository;
import t1internship.authservice.service.AuthService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtInPort jwtInPort;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserKafkaProducer userKafkaProducer;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private SignInRequest signInRequest;
    private SignUpRequest signUpRequest;
    private RefreshTokenRequest refreshTokenRequest;
    private User user;
    private Role guestRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        signInRequest = new SignInRequest("user@example.com", "password");
        signUpRequest = new SignUpRequest("user@example.com",  "password", "password");
        refreshTokenRequest = new RefreshTokenRequest("refreshToken");

        guestRole = new Role();
        guestRole.setName("ROLE_GUEST");

        adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");
        user.setRoles(List.of(guestRole));
    }

    @Test
    void login_ValidCredentials_ReturnsAuthResponse() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtInPort.generateAccessToken(user.getEmail())).thenReturn("accessToken");
        when(jwtInPort.generateRefreshToken(user.getEmail())).thenReturn("refreshToken");

        AuthResponse response = authService.login(signInRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtInPort, times(1))
                .generateAccessToken(user.getEmail());
        verify(jwtInPort, times(1))
                .generateRefreshToken(user.getEmail());
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(signInRequest));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtInPort);
    }

    @Test
    void register_ValidRequest_SavesUser() {
        when(userRepository.findByEmailIgnoreCase(signUpRequest.email())).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_GUEST")).thenReturn(Optional.of(guestRole));
        when(userMapper.signUpRequestToUser(signUpRequest)).thenReturn(user);
        when(passwordEncoder.encode(signUpRequest.password())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        authService.register(signUpRequest);

        verify(userRepository, times(1)).findByEmailIgnoreCase(signUpRequest.email());
        verify(roleRepository, times(1)).findByName("ROLE_GUEST");
        verify(userMapper, times(1)).signUpRequestToUser(signUpRequest);
        verify(passwordEncoder, times(1)).encode(signUpRequest.password());
        verify(userRepository, times(1)).save(user);
        verify(userKafkaProducer, times(1)).sendUser(any());
    }

    @Test
    void register_EmailExists_ThrowsException() {
        when(userRepository.findByEmailIgnoreCase(signUpRequest.email())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> authService.register(signUpRequest));

        verify(userRepository, times(1)).findByEmailIgnoreCase(signUpRequest.email());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(roleRepository, userMapper, passwordEncoder, userKafkaProducer);
    }

    @Test
    void register_InvalidEmail_ThrowsException() {
        SignUpRequest invalidRequest = new SignUpRequest("invalid.com", "password", "password");

        assertThrows(RuntimeException.class, () -> authService.register(invalidRequest));

        verify(userRepository, times(1)).findByEmailIgnoreCase(invalidRequest.email());
        verifyNoInteractions(roleRepository, userMapper, passwordEncoder, userKafkaProducer);
    }

    @Test
    void register_PasswordMismatch_ThrowsException() {
        SignUpRequest invalidRequest = new SignUpRequest("user@example.com", "password", "different");

        assertThrows(RuntimeException.class, () -> authService.register(invalidRequest));

        verify(userRepository, times(1)).findByEmailIgnoreCase(invalidRequest.email());
        verifyNoInteractions(roleRepository, userMapper, passwordEncoder, userKafkaProducer);
    }

    @Test
    void register_RoleNotFound_ThrowsException() {
        when(userRepository.findByEmailIgnoreCase(signUpRequest.email())).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_GUEST")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authService.register(signUpRequest));

        verify(userRepository, times(1)).findByEmailIgnoreCase(signUpRequest.email());
        verify(roleRepository, times(1)).findByName("ROLE_GUEST");
        verifyNoMoreInteractions(userRepository, roleRepository);
        verifyNoInteractions(userMapper, passwordEncoder, userKafkaProducer);
    }

    @Test
    void logout_ValidEmail_DropsTokens() {
        String email = "user@example.com";

        authService.logout(email);

        verify(jwtInPort, times(1)).dropAllUserTokens(email);
    }

    @Test
    void refreshToken_ValidRequest_ReturnsNewTokens() {
        when(jwtInPort.refreshToken(refreshTokenRequest.refreshToken())).thenReturn("newAccessToken");

        AuthResponse response = authService.refreshToken(refreshTokenRequest);

        assertNotNull(response);
        assertEquals("newAccessToken", response.accessToken());
        assertEquals(refreshTokenRequest.refreshToken(), response.refreshToken());

        verify(jwtInPort, times(1)).refreshToken(refreshTokenRequest.refreshToken());
    }
}