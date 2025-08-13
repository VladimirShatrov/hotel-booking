package t1internship.security.configuration.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtSecurityProperties(
        String jwkSetUri,
        List<String> publicUrls
) {
    public String[] getPublicUrls() {
        return publicUrls != null ?
                publicUrls.toArray(new String[0]) :
                new String[0];
    }
}