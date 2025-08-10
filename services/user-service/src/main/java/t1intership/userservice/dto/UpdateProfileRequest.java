package t1intership.userservice.dto;

public record UpdateProfileRequest(
        String firstName,
        String lastName,
        Long departmentId,
        String jobTitle
) {
}
