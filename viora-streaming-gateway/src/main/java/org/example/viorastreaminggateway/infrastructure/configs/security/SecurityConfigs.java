package org.example.viorastreaminggateway.infrastructure.configs.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Configuration
public class SecurityConfigs {
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String SERVICE_ROLE = "SERVICE";

    @Bean
    public SecurityWebFilterChain httpSecurity(ServerHttpSecurity httpSecurity) {
        httpSecurity.oauth2ResourceServer(configurer -> configurer.jwt(Customizer.withDefaults()));
        httpSecurity.cors(Customizer.withDefaults());

        httpSecurity.authorizeExchange(exchange -> {
            exchange.pathMatchers("/api/identity/api/v1/admin/accounts/keycloak")
                    .hasRole(SERVICE_ROLE);
            exchange.pathMatchers("/api/identity/api/v1/admin/accounts")
                    .hasRole(ADMIN_ROLE);
            exchange.pathMatchers("/api/content/api/v1/movies", "/api/content/api/v1/actors")
                    .hasRole(ADMIN_ROLE);
            exchange.pathMatchers("/actuator/health").permitAll();
            exchange.pathMatchers("/swagger-ui/*", "/v3/api-docs", "/v3/api-docs.yaml", "/v3/api-docs/*")
                    .permitAll();
            exchange.pathMatchers(HttpMethod.OPTIONS).permitAll();
            exchange.anyExchange().authenticated();
        });

        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration streamingCorsConfiguration = new CorsConfiguration();
        streamingCorsConfiguration.setAllowedOrigins(List.of("http://localhost:63342", "http://localhost:63343"));
        streamingCorsConfiguration.setAllowedMethods(List.of("GET", "OPTIONS", "HEAD"));
        streamingCorsConfiguration.setAllowedHeaders(List.of("Authorization", "Range", "Content-Type"));
        streamingCorsConfiguration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/streaming/**", streamingCorsConfiguration);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        converter.setPrincipalClaimName("sub");
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            List<String> roles = (List<String>) jwt.getClaimAsMap("realm_access").get("roles");

            return Stream.concat(authorities.stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();
        });

        return converter;
    }

}
