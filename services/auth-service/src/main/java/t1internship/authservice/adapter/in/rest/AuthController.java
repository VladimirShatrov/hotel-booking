package t1internship.authservice.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t1internship.authservice.dto.AuthResponse;
import t1internship.authservice.dto.RefreshTokenRequest;
import t1internship.authservice.dto.SignInRequest;
import t1internship.authservice.dto.SignUpRequest;
import t1internship.authservice.port.in.AuthInPort;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthInPort authInPort;

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody final SignInRequest request) {
        return ResponseEntity.ok()
                .body(authInPort.login(request));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logout(@RequestBody final Authentication principal) {
        this.authInPort.logout(principal.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> register(@RequestBody final SignUpRequest request) {
        this.authInPort.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody
            final RefreshTokenRequest request
    ){
        return ResponseEntity.ok(this.authInPort.refreshToken(request));
    }
}
