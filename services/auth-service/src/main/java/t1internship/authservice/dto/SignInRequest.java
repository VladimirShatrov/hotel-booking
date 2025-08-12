package t1internship.authservice.dto;

public record SignInRequest(
        String email,
        String password
) {
}