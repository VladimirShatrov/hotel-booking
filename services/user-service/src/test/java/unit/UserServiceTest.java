package unit;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.relax.userservice.domain.User;
import org.relax.userservice.dto.UpdateProfileRequest;
import org.relax.userservice.dto.UserData;
import org.relax.userservice.mapper.UserMapper;
import org.relax.userservice.repository.UserRepository;
import org.relax.userservice.service.UserService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;


    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUserDataById_UserFound_ReturnsValidDto() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "name", "lastName",
                "email", "username");

        UserData expectedDto = new UserData( id, "name", "lastName",
                "email", "username");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(mapper.entityToDto(user)).thenReturn(expectedDto);


        var response = userService.getUserDataById(id);

        assertNotNull(response);
        assertEquals(expectedDto, response);

        verify(userRepository, times(1)).findById(id);
        verify(mapper, times(1)).entityToDto(user);
    }

    @Test
    public void getUserDataById_UserNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserDataById(id));

        verify(userRepository, times(1)).findById(id);
        verify(mapper, never()).entityToDto(any());
    }

    @Test
    public void updateProfile_ValidRequestData_ReturnsValidDto() {
        UUID id = UUID.randomUUID();

        User userProfileBeforeUpdate = User.builder()
                .id(id)
                .email("email")
                .firstName("firstNameBeforeUpdate")
                .lastName("lastNameBeforeUpdate")
                .username("usernameBeforeUpdate")
                .build();

        UpdateProfileRequest request = new UpdateProfileRequest("first", "last",
                "username");

        User userToSave = User.builder()
                .id(id)
                .email("email")
                .firstName("first")
                .lastName("last")
                .username("username")
                .build();

        UserData expectedDto = new UserData(id, "first", "last",
                "email", "username");


        when(userRepository.findById(id))
                .thenReturn(Optional.of(userProfileBeforeUpdate));
        when(userRepository.save(any(User.class)))
                .thenReturn(userToSave);
        when(mapper.entityToDto(userToSave))
                .thenReturn(expectedDto);

        var response = userService.updateProfile(id, request);


        assertNotNull(response);
        assertEquals(expectedDto, response);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(any(User.class));
        verify(mapper, times(1)).entityToDto(any(User.class));
    }

    @Test
    public void updateProfile_UserNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        UpdateProfileRequest request = new UpdateProfileRequest(null, null, null);

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateProfile(id, request));

        verify(userRepository, times(1)).findById(id);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void updateProfile_PartialUpdate_UpdatesOnlyProvidedFields() {
        UUID id = UUID.randomUUID();
        User userProfileBeforeUpdate = User.builder()
                .id(id)
                .email("email")
                .firstName("firstNameBeforeUpdate")
                .lastName("lastNameBeforeUpdate")
                .username("usernameBeforeUpdate")
                .build();

        UpdateProfileRequest request = new UpdateProfileRequest("newFirstName", null,
                "newUserName");

        User savedUser = User.builder()
                .id(id)
                .email("email")
                .firstName("newFirstName")
                .lastName("lastNameBeforeUpdate")
                .username("newUsername")
                .build();

        UserData expectedDto = new UserData(id, "newFirstName", "lastNameBeforeUpdate",
                "email", "newUsername");

        when(userRepository.findById(id)).thenReturn(Optional.of(userProfileBeforeUpdate));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(mapper.entityToDto(savedUser)).thenReturn(expectedDto);

        var response = userService.updateProfile(id, request);

        assertNotNull(response);
        assertEquals("newFirstName", response.firstName());
        assertEquals("lastNameBeforeUpdate", response.lastName());
        assertEquals("newUsername", response.username());

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(any(User.class));
        verify(mapper, times(1)).entityToDto(any(User.class));
    }
}
