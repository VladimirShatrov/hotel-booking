package org.relax.authservice.port.in;

import org.relax.authservice.dto.AuthResponse;
import org.relax.authservice.dto.RefreshTokenRequest;
import org.relax.authservice.dto.SignInRequest;
import org.relax.authservice.dto.SignUpRequest;

public interface AuthInPort {

    AuthResponse login(SignInRequest request);

    void register (SignUpRequest request);

    void logout(String userEmail);

    AuthResponse refreshToken(RefreshTokenRequest request);
}
