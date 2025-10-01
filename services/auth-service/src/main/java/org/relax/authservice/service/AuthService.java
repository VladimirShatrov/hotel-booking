package org.relax.authservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.relax.authservice.adapter.out.kafka.UserKafkaProducer;
import org.relax.authservice.domain.Role;
import org.relax.authservice.domain.User;
import org.relax.authservice.dto.AuthResponse;
import org.relax.authservice.dto.RefreshTokenRequest;
import org.relax.authservice.dto.SignInRequest;
import org.relax.authservice.dto.SignUpRequest;
import org.relax.authservice.handler.exception.EmailNotValidException;
import org.relax.authservice.handler.exception.PasswordDisMatchException;
import org.relax.authservice.mapper.UserMapper;
import org.relax.authservice.port.in.AuthInPort;
import org.relax.authservice.port.in.JwtInPort;
import org.relax.authservice.port.out.RoleRepository;
import org.relax.authservice.port.out.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthInPort {

    private final AuthenticationManager authenticationManager;
    private final JwtInPort jwtInPort;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserKafkaProducer userKafkaProducer;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(SignInRequest request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        final User user = (User) authentication.getPrincipal();
        final String accessToken = this.jwtInPort.generateAccessToken(user.getEmail());
        final String refreshToken = this.jwtInPort.generateRefreshToken(user.getEmail());

        return new AuthResponse(
                accessToken,
                refreshToken
        );
    }

    @Override
    @Transactional
    public void register(SignUpRequest request) {
        checkUserEmail(request.email());
        checkPassword(request.password(), request.confirmPassword());
        final Role userRole;
        userRole = this.roleRepository.findByName("ROLE_GUEST")
                    .orElseThrow(() -> new EntityNotFoundException("не удалось найти роль: ROLE_GUEST"));
        final List<Role> roles = new ArrayList<>();
        roles.add(userRole);

        final User user = this.userMapper.signUpRequestToUser(request);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        User sendUser = this.userRepository.save(user);
        this.userKafkaProducer.sendUser(userMapper.entityToKafkaData(sendUser));
    }

    @Override
    public void logout(String userEmail) {
        this.jwtInPort.dropAllUserTokens(userEmail);
    }

    private void checkPassword(String password, String confirmPassword) {
        if (password == null || !password.equals(confirmPassword)) {
            throw new PasswordDisMatchException("Пароли не совпадают");
        }
    }

    private void checkUserEmail(String email) {
        final boolean emailExists = this.userRepository.findByEmailIgnoreCase(email).isPresent();
        if (emailExists || !email.contains("@")) {
            throw new EmailNotValidException("Не верный email");
        }
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        final String newAccessToken = this.jwtInPort.refreshToken(request.refreshToken());
        return new AuthResponse(newAccessToken, request.refreshToken());
    }
}
