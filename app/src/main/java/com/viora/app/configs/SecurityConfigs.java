package com.viora.app.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

// Notes: This configs will stay here until we move to microservices
@Configuration
public class SecurityConfigs {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String SERVICE_ROLE = "SERVICE";

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity httpSecurity) throws
            Exception {

        httpSecurity.oauth2ResourceServer(configurer -> {
            configurer.jwt(Customizer.withDefaults());
        });

        httpSecurity.authorizeHttpRequests(requestRegistry -> {
            requestRegistry.requestMatchers("/api/v1/admin/accounts/keycloak")
                    .hasRole(SERVICE_ROLE);
            requestRegistry.requestMatchers("/api/v1/admin/accounts", "/api/v1/movies", "/api/v1/actors")
                    .hasRole(ADMIN_ROLE);
            requestRegistry.requestMatchers("/swagger-ui/*", "/v3/api-docs", "/v3/api-docs.yaml", "/v3/api-docs/*")
                    .permitAll();
            requestRegistry.requestMatchers("/actuator/health")
                    .permitAll();
            requestRegistry.anyRequest()
                    .authenticated();
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        converter.setPrincipalClaimName("sub");
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            List<String> roles = (List<String>) jwt.getClaimAsMap("realm_access")
                    .get("roles");

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
