package org.relax.authservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.relax.authservice.logger.ILogger;
import org.relax.authservice.logger.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.relax.authservice.domain.Role;
import org.relax.authservice.domain.User;
import org.relax.authservice.dto.ChangePasswordRequest;
import org.relax.authservice.dto.UserData;
import org.relax.authservice.handler.exception.PasswordDisMatchException;
import org.relax.authservice.handler.exception.RoleException;
import org.relax.authservice.handler.exception.UserNotEnabledException;
import org.relax.authservice.mapper.UserMapper;
import org.relax.authservice.port.in.UserInPort;
import org.relax.authservice.port.out.RoleRepository;
import org.relax.authservice.port.out.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserInPort {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    private final RoleRepository roleRepository;
    //
    private final LoggerFactory loggerFactory;

    @Override
    public void changePassword(ChangePasswordRequest request, UUID userId) {
        //
        ILogger logger = loggerFactory.createLogger(LoggerFactory.LoggerType.USER);
        logger.log("Changing password for user " + userId);

        if (!request.newPassword().equals(request.newPasswordConfirm())) {
            throw new PasswordDisMatchException("Пароли не совпадают: " + request.newPassword() +
                    ", " + request.newPasswordConfirm());
        }

        final User savedUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + userId + " не найден"));

        if (!this.passwordEncoder.matches(request.oldPassword(), savedUser.getPassword())) {
            throw new PasswordDisMatchException("Неверный пароль");
        }

        final String encodedPassword = passwordEncoder.encode(request.newPassword());
        savedUser.setPassword(encodedPassword);
        this.userRepository.save(savedUser);
    }

    @Override
    public void deactivateAccount(UUID userId) {
        final User savedUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + userId + " не найден"));

        if (!savedUser.isEnabled()) {
            throw new UserNotEnabledException("Пользователь недоступен");
        }
        savedUser.setEnabled(false);
        this.userRepository.save(savedUser);
    }

    @Override
    public void reactivateAccount(UUID userId) {
        final User savedUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + userId + " не найден"));

        if (savedUser.isEnabled()) {
            throw new IllegalArgumentException("Аккаунт уже доступен");
        }
        savedUser.setEnabled(true);
        this.userRepository.save(savedUser);
    }

    @Override
    public UserData findUserByEmail(String email) {
        return mapper.entityToDto(userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с email: " + email + " не найден")));
    }

    @Override
    public void giveUserAdminRole(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + userId + " не найден"));

        List<Role> userRoles = new ArrayList<>(user.getRoles());
        if (userRoles.stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            throw new RoleException("У пользователя: " + userId + " уже есть роль ADMIN");
        }

        userRoles.add(roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new EntityNotFoundException("Роль ROLE_ADMIN не найдена")));
        user.setRoles(userRoles);
        userRepository.save(user);
    }
}