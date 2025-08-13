package t1internship.authservice.dto;

public record SignUpRequest(
        String email,
        String password,
        String confirmPassword
) {
}