package org.relax.authservice.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.relax.authservice.dto.ChangePasswordRequest;
import org.relax.authservice.port.in.UserInPort;

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

    @PostMapping("/admin/{userEmail}/give/admin/role")
    public ResponseEntity<Void> giveAdminRole(@PathVariable String userEmail) {
        userInPort.giveUserAdminRole(userInPort.findUserByEmail(userEmail).id());
        return ResponseEntity.ok().build();
    }

}
