package org.relax.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtUserEmailHeaderFilter extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            return exchange.getPrincipal()
                    .filter(principal -> principal instanceof AbstractAuthenticationToken)
                    .cast(AbstractAuthenticationToken.class)
                    .map(authentication -> {
                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header("X-User-Email", authentication.getName())
                                .build();
                        return exchange.mutate().request(modifiedRequest).build();
                    })
                    .defaultIfEmpty(exchange)
                    .flatMap(chain::filter);
        });
    }
}
