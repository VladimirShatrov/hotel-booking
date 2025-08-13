package t1internship.authservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import t1internship.authservice.domain.User;
import t1internship.authservice.dto.ChangePasswordRequest;
import t1internship.authservice.mapper.UserMapper;
import t1internship.authservice.port.in.UserInPort;
import t1internship.authservice.port.out.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserInPort {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(ChangePasswordRequest request, UUID userId) {
        if (!request.newPassword().equals(request.newPasswordConfirm())) {
            throw new RuntimeException("Пароли не совпадают: " + request.newPassword() + ", " + request.newPasswordConfirm());
        }

        final User savedUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + userId + " не найден"));

        if (!this.passwordEncoder.matches(request.oldPassword(), savedUser.getPassword())) {
            throw new RuntimeException("неверный пароль");
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
            throw new RuntimeException("Пользователь недоступен");
        }
        savedUser.setEnabled(false);
        this.userRepository.save(savedUser);
    }

    @Override
    public void reactivateAccount(UUID userId) {
        final User savedUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + userId + " не найден"));

        if (!savedUser.isEnabled()) {
            throw new RuntimeException("Пользователь недоступен");
        }
        savedUser.setEnabled(true);
        this.userRepository.save(savedUser);
    }
}
