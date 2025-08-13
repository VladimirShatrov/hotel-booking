package t1internship.authservice.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String newPasswordConfirm
) {
}
