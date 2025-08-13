package t1intership.userservice.dto;

import java.util.UUID;

public record UserData(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String departmentTitle,
        String jobTitle
) {
}
