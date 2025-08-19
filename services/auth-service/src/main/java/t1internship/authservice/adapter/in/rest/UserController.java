package t1internship.authservice.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import t1internship.authservice.dto.ChangePasswordRequest;
import t1internship.authservice.port.in.UserInPort;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/user")
public class UserController {

    private final UserInPort userInPort;

    @PostMapping("/{userId}/password/change")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequest request,
            @PathVariable UUID userId
    ) {
        userInPort.changePassword(request, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable UUID userId
    ) {
        userInPort.deactivateAccount(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/reactivate")
    public ResponseEntity<Void> reactivate(
            @PathVariable UUID userId
    ) {
        userInPort.reactivateAccount(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/give/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> giveAdminRole(@PathVariable UUID userId) {
        userInPort.giveUserAdminRole(userId);
        return ResponseEntity.ok().build();
    }

}
