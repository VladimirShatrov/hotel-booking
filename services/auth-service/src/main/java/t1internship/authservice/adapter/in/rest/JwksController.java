package t1internship.authservice.adapter.in.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import t1internship.authservice.config.KeyUtils;

import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
public class JwksController {

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getJwkSet() {
        try {
            RSAPublicKey publicKey = (RSAPublicKey) KeyUtils.loadPublicKey("keys/public_key.pem");

            return Map.of(
                    "keys", List.of(
                            Map.of(
                                    "kty", "RSA",
                                    "kid", "main-key",
                                    "n", Base64.getUrlEncoder().withoutPadding()
                                            .encodeToString(publicKey.getModulus().toByteArray()),
                                    "e", Base64.getUrlEncoder().withoutPadding()
                                            .encodeToString(publicKey.getPublicExponent().toByteArray()),
                                    "alg", "RS256",
                                    "use", "sig"
                            )
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JWKS", e);
        }
    }
}