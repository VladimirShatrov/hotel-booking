package t1internship.security.configuration.starter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@AutoConfiguration
@EnableMethodSecurity
@EnableConfigurationProperties(JwtSecurityProperties.class)
public class SecurityAutoConfiguration {

    private final JwtAuthConverter jwtAuthConverter;
    private final JwtSecurityProperties properties;

    public SecurityAutoConfiguration(JwtAuthConverter jwtAuthConverter,
                                     JwtSecurityProperties properties) {
        this.jwtAuthConverter = jwtAuthConverter;
        this.properties = properties;
    }

    @Bean
    public SecurityWebFilterChain WebSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(properties.getPublicUrls()).permitAll()
                        .pathMatchers("/api/**").authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }
    @Bean(name = "customJwtDecoder")
    @ConditionalOnMissingBean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(properties.jwkSetUri()).build();
    }
}