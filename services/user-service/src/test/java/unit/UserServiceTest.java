package unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import t1intership.userservice.domain.Department;
import t1intership.userservice.domain.User;
import t1intership.userservice.dto.UpdateProfileRequest;
import t1intership.userservice.dto.UserData;
import t1intership.userservice.mapper.UserMapper;
import t1intership.userservice.repository.DepartmentRepository;
import t1intership.userservice.repository.UserRepository;
import t1intership.userservice.service.UserService;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void getUserDataById_UserFound_ReturnsValidDto() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "name", "lastName",
                "email", new Department(1L, "department"), "job");

        UserData expectedDto = new UserData( id, "name", "lastName",
                "email", "department", "job");

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

        assertThrows(NoSuchElementException.class, () -> userService.getUserDataById(id));

        verify(userRepository, times(1)).findById(id);
        verify(mapper, never()).entityToDto(any());
    }

    @Test
    public void updateProfile_ValidRequestData_ReturnsValidDto() {
        UUID id = UUID.randomUUID();
        Long departmentId = 1L;

        User userProfileBeforeUpdate = User.builder()
                .id(id)
                .email("email")
                .firstName("firstNameBeforeUpdate")
                .lastName("lastNameBeforeUpdate")
                .department(new Department(2L, "departmentBeforeUpdate"))
                .jobTitle("jobBeforeUpdate")
                .build();

        UpdateProfileRequest request = new UpdateProfileRequest("first", "last",
                departmentId, "job");

        User userToSave = User.builder()
                .id(id)
                .email("email")
                .firstName("first")
                .lastName("last")
                .department(new Department(departmentId, "department"))
                .jobTitle("job")
                .build();

        UserData expectedDto = new UserData(id, "first", "last",
                "email", "department", "job");


        when(userRepository.findById(id))
                .thenReturn(Optional.of(userProfileBeforeUpdate));
        when(userRepository.save(any(User.class)))
                .thenReturn(userToSave);
        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.of(new Department(departmentId, "department")));
        when(mapper.entityToDto(userToSave))
                .thenReturn(expectedDto);

        var response = userService.updateProfile(id, request);


        assertNotNull(response);
        assertEquals(expectedDto, response);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(any(User.class));
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(mapper, times(1)).entityToDto(any(User.class));
    }

    @Test
    public void updateProfile_UserNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        UpdateProfileRequest request = new UpdateProfileRequest(null, null, null, null);

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.updateProfile(id, request));

        verify(userRepository, times(1)).findById(id);
        verifyNoInteractions(departmentRepository);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void updateProfile_DepartmentNotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        Long departmentId = 1L;
        UpdateProfileRequest request = new UpdateProfileRequest(null, null, departmentId, null);
        User user = User.builder().id(id).build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.updateProfile(id, request));

        verify(userRepository, times(1)).findById(id);
        verify(departmentRepository, times(1)).findById(departmentId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    public void updateProfile_PartialUpdate_UpdatesOnlyProvidedFields() {
        UUID id = UUID.randomUUID();
        User userProfileBeforeUpdate = User.builder()
                .id(id)
                .email("email")
                .firstName("firstNameBeforeUpdate")
                .lastName("lastNameBeforeUpdate")
                .department(new Department(2L, "departmentBeforeUpdate"))
                .jobTitle("jobBeforeUpdate")
                .build();

        UpdateProfileRequest request = new UpdateProfileRequest("newFirstName", null,
                null, "newJob");

        User savedUser = User.builder()
                .id(id)
                .email("email")
                .firstName("newFirstName")
                .lastName("lastNameBeforeUpdate")
                .department(new Department(2L, "departmentBeforeUpdate"))
                .jobTitle("newJob")
                .build();

        UserData expectedDto = new UserData(id, "newFirstName", "lastNameBeforeUpdate",
                "email", "departmentBeforeUpdate", "newJob");

        when(userRepository.findById(id)).thenReturn(Optional.of(userProfileBeforeUpdate));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(mapper.entityToDto(savedUser)).thenReturn(expectedDto);

        var response = userService.updateProfile(id, request);

        assertNotNull(response);
        assertEquals("newFirstName", response.firstName());
        assertEquals("lastNameBeforeUpdate", response.lastName());
        assertEquals("newJob", response.jobTitle());

        verify(userRepository, times(1)).findById(id);
        verifyNoInteractions(departmentRepository);
        verify(userRepository, times(1)).save(any(User.class));
        verify(mapper, times(1)).entityToDto(any(User.class));
    }
}
