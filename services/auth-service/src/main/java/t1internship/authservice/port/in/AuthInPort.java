package t1internship.authservice.port.in;

import t1internship.authservice.dto.AuthResponse;
import t1internship.authservice.dto.RefreshTokenRequest;
import t1internship.authservice.dto.SignInRequest;
import t1internship.authservice.dto.SignUpRequest;

public interface AuthInPort {

    AuthResponse login(SignInRequest request);

    void register (SignUpRequest request);

    void logout(String userEmail);

    AuthResponse refreshToken(RefreshTokenRequest request);
}
