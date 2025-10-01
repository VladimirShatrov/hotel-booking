package org.relax.authservice.unit;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.relax.authservice.domain.Role;
import org.relax.authservice.domain.User;
import org.relax.authservice.dto.ChangePasswordRequest;
import org.relax.authservice.dto.UserData;
import org.relax.authservice.handler.exception.PasswordDisMatchException;
import org.relax.authservice.handler.exception.UserNotEnabledException;
import org.relax.authservice.mapper.UserMapper;
import org.relax.authservice.port.out.RoleRepository;
import org.relax.authservice.port.out.UserRepository;
import org.relax.authservice.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper mapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    UserService userService;

    @Test
    public void changePassword_ValidRequest_ValidUserId_PasswordChanged() {
        ChangePasswordRequest request = new ChangePasswordRequest
                ("old", "new", "new");
        UUID userId = UUID.randomUUID();
        String encodedOldPassword = "encodedOld";
        User user = User.builder()
                .id(userId)
                .password(encodedOldPassword)
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.oldPassword(), user.getPassword()))
                .thenReturn(true);
        when(passwordEncoder.encode(request.newPassword()))
                .thenReturn("encodedNew");

        userService.changePassword(request, userId);

        assertEquals(user.getPassword(), "encodedNew");

        verify(userRepository, times(1))
                .findById(userId);
        verify(userRepository, times(1))
                .save(any(User.class));
        verifyNoMoreInteractions(userRepository);

        verify(passwordEncoder, times(1))
                .matches(request.oldPassword(), encodedOldPassword);
        verify(passwordEncoder, times(1))
                .encode(request.newPassword());
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    public void changePassword_ValidRequest_InvalidUserId_ThrowsEntityNotFoundException() {
        ChangePasswordRequest request = new ChangePasswordRequest
                ("old", "new", "new");

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.changePassword(request, UUID.randomUUID()));

        verify(userRepository, times(1))
                .findById(any(UUID.class));
        verifyNoMoreInteractions(userRepository);

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void changePassword_InValidOldPasswordInRequest_ValidUserId_ThrowsPasswordDisMatchException() {
        ChangePasswordRequest request = new ChangePasswordRequest(
                "wrong", "new", "new"
        );
        UUID userId = UUID.randomUUID();
        String encodedOldPassword = "encodedOld";
        User user = User.builder()
                .id(userId)
                .password(encodedOldPassword)
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.oldPassword(), encodedOldPassword))
                .thenReturn(false);

        assertThrows(PasswordDisMatchException.class,
                () -> userService.changePassword(request, userId));
        assertEquals(user.getPassword(), encodedOldPassword);

        verify(userRepository, times(1))
                .findById(userId);
        verifyNoMoreInteractions(userRepository);

        verify(passwordEncoder, times(1))
                .matches(request.oldPassword(), encodedOldPassword);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    public void changePassword_InvalidConfirmPasswordInRequest_ThrowsPasswordDisMatchException() {
        ChangePasswordRequest request = new ChangePasswordRequest(
                "old", "new", "wrong"
        );

        assertThrows(PasswordDisMatchException.class,
                () -> userService.changePassword(request, UUID.randomUUID()));
        verifyNoInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void deactivateAccount_ValidUserId_AccountIsEnabled_AccountDeactivated() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .enabled(true)
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.deactivateAccount(userId);

        assertFalse(user.isEnabled());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void deactivateAccount_ValidUserId_AccountIsNotEnabled_ThrowsUserNotEnabledException() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .enabled(false)
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        assertThrows(UserNotEnabledException.class,
                () -> userService.deactivateAccount(userId));
        assertFalse(user.isEnabled());

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void deactivateAccount_InValidUserId_ThrowsEntityNotFoundException() {
        when(userRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.deactivateAccount(UUID.randomUUID()));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void reactivateAccount_ValidUserId_AccountIsNotEnabled_AccountReactivated() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .enabled(false)
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.reactivateAccount(userId);

        assertTrue(user.isEnabled());
        verify(userRepository, times(1))
                .findById(userId);
        verify(userRepository, times(1))
                .save(user);
    }

    @Test
    public void reactivateAccount_ValidUserId_AccountEnabled_ThrowsIllegalArgumentException() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .enabled(true)
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class,
                () -> userService.reactivateAccount(userId));
        assertTrue(user.isEnabled());

        verify(userRepository, times(1))
                .findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void reactivateAccount_InValidUserId_ThrowsEntityNotFoundException() {
        when(userRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.reactivateAccount(UUID.randomUUID()));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findUserByEmail_ValidEmail_ReturnsValidUserData() {
        String email = "@mail.com";
        User user = User.builder()
                .email(email)
                .enabled(true)
                .locked(false)
                .credentialsExpired(false)
                .build();
        UserData expectedData = new UserData(null, email,
                null, false, true, false);

        when(userRepository.findByEmailIgnoreCase(email))
                .thenReturn(Optional.of(user));
        when(mapper.entityToDto(user))
                .thenReturn(expectedData);

        var response = userService.findUserByEmail(email);

        assertNotNull(response);
        assertEquals(expectedData, response);

        verify(userRepository, times(1))
                .findByEmailIgnoreCase(email);
        verify(mapper, times(1))
                .entityToDto(user);
    }

    @Test
    public void findUserByEmail_InValidEmail_ThrowsEntityNotFoundException() {
        when(userRepository.findByEmailIgnoreCase(any(String.class)))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.findUserByEmail("wrong@mail.com"));

        verify(userRepository, times(1))
                .findByEmailIgnoreCase(any(String.class));
        verifyNoInteractions(mapper);
    }

    @Test
    public void giveUserAdminRole_ValidUserId_UserDoesNotHaveAdminRole_AdminRoleEnabled_AddAdminRoleToUser() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .roles(new ArrayList<>())
                .build();
        UUID roleId = UUID.randomUUID();
        Role role = new Role(roleId, "ROLE_ADMIN", List.of());

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_ADMIN"))
                .thenReturn(Optional.of(role));

        userService.giveUserAdminRole(userId);
        assertThat(user.getRoles()).contains(role);
    }
}