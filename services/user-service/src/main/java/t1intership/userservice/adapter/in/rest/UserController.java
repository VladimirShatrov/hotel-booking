package t1intership.userservice.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t1intership.userservice.dto.UpdateProfileRequest;
import t1intership.userservice.dto.UserData;
import t1intership.userservice.port.in.UserInPort;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

    private static final String MEDIA_TYPE = "application/vnd.t1internship.user.v1+json";

    private final UserInPort userService;

    @GetMapping(
            produces = MEDIA_TYPE,
            path = "/me"
    )
    public ResponseEntity<UserData> getMyProfile(
            @RequestHeader("X-User-Email") String email
    ) {
        UserData userData = userService.getUserDataByEmail(email);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MEDIA_TYPE))
                .body(userData);
    }

    @GetMapping(
            produces = MEDIA_TYPE,
            path = "/{userId}"
    )
    public ResponseEntity<UserData> getUserProfile(
            @PathVariable UUID userId
            ) {
        UserData user = userService.getUserDataById(userId);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MEDIA_TYPE))
                .body(user);
    }

    @PutMapping(
            produces = MEDIA_TYPE,
            path = "/{userId}")
    public ResponseEntity<UserData> updateUserProfile(
            @PathVariable UUID userId,
            @RequestBody UpdateProfileRequest request
            ) {

        UserData user = userService.updateProfile(userId, request);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MEDIA_TYPE))
                .body(user);
    }

    @GetMapping("/{userId}/exists")
    public ResponseEntity<Void> exists(
            @PathVariable UUID userId
    ) {
        return userService.existsById(userId)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

}
