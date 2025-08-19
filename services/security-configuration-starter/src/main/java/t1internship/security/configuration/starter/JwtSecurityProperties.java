package t1internship.security.configuration.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtSecurityProperties(
        String jwkSetUri,
        List<String> publicUrls
) {
    public JwtSecurityProperties {
        jwkSetUri = jwkSetUri != null ? jwkSetUri : "";
        publicUrls = publicUrls != null ? publicUrls : List.of();
    }

    public String[] getPublicUrls() {
        return publicUrls.toArray(new String[0]);
    }
}