package t1internship.security.configuration.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Arrays;

@AutoConfiguration
@EnableMethodSecurity
@EnableConfigurationProperties(JwtSecurityProperties.class)
@Slf4j
public class SecurityAutoConfiguration {

    private static final String[] PUBLIC_URLS = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/.well-known/jwks.json",
            "/api/v1/auth/**"
    };

    private static final String JWK_SET_URI = "http://auth-service:8006/.well-known/jwks.json";


    private final JwtAuthConverter jwtAuthConverter;
    private final JwtSecurityProperties properties;

    public SecurityAutoConfiguration(JwtAuthConverter jwtAuthConverter,
                                     JwtSecurityProperties properties) {
        this.jwtAuthConverter = jwtAuthConverter;
        this.properties = properties;
        log.info("SECURITY PUBLIC URLS: {}", Arrays.toString(properties.getPublicUrls()));
    }

    @Bean
    public SecurityWebFilterChain WebSecurityFilterChain(ServerHttpSecurity http) {
        if (properties.getPublicUrls().length == 0) {
            log.warn("NO PUBLIC URLS");
        }
        return http
                .authorizeExchange(exchanges -> {
                    if (properties.getPublicUrls().length > 0) {
                        exchanges.pathMatchers(PUBLIC_URLS).permitAll();
                    }
                    exchanges.anyExchange().authenticated();
                })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean(name = "customJwtDecoder")
    @ConditionalOnMissingBean
    public ReactiveJwtDecoder jwtDecoder() {
        if (properties.jwkSetUri().isEmpty()) {
            log.warn("JWK URI IS EMPTY: {}", properties.jwkSetUri());
            return NimbusReactiveJwtDecoder.withJwkSetUri(JWK_SET_URI).build();
        }
        return NimbusReactiveJwtDecoder.withJwkSetUri(properties.jwkSetUri()).build();
    }
}