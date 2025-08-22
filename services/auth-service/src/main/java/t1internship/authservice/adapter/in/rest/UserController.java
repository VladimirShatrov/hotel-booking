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

    @PostMapping("/me/password/change")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequest request,
            @RequestHeader("X-User-Email") String userEmail
    ) {
        userInPort.changePassword(request, userInPort.findUserByEmail(userEmail).id());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/deactivate")
    public ResponseEntity<Void> deactivate(
            @RequestHeader("X-User-Email") String userEmail
    ) {
        userInPort.deactivateAccount(userInPort.findUserByEmail(userEmail).id());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/reactivate")
    public ResponseEntity<Void> reactivate(
            @RequestHeader("X-User-Email") String userEmail
    ) {
        userInPort.reactivateAccount(userInPort.findUserByEmail(userEmail).id());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userEmail}/give/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> giveAdminRole(@PathVariable String userEmail) {
        userInPort.giveUserAdminRole(userInPort.findUserByEmail(userEmail).id());
        return ResponseEntity.ok().build();
    }

}
