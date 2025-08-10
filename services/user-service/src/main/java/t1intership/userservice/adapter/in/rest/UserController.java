package t1intership.userservice.adapter.in.rest;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t1intership.userservice.dto.UpdateProfileRequest;
import t1intership.userservice.dto.UserData;
import t1intership.userservice.mapper.UserMapper;
import t1intership.userservice.port.in.UserInPort;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/v1")
public class UserController {

    private static final String MEDIA_TYPE = "application/vnd.t1intership.user.v1+json";

    private final UserInPort userService;

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

    @PutMapping(path = "/{userId}")
    public ResponseEntity<UserData> updateUserProfile(
            @PathVariable UUID userId,
            @RequestBody UpdateProfileRequest request
            ) {

        UserData user = userService.updateProfile(userId, request);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MEDIA_TYPE))
                .body(user);
    }

}
