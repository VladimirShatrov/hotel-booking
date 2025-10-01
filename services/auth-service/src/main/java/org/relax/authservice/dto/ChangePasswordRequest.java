package org.relax.authservice.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String newPasswordConfirm
) {
}
