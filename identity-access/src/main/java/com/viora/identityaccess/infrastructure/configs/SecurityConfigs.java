package com.viora.identityaccess.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class SecurityConfigs {
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String SERVICE_ROLE = "SERVICE";

    @Bean
    public SecurityFilterChain httpSecurity(
            HttpSecurity httpSecurity,
            GatewayHeadersFilter gatewayHeadersFilter
    ) throws Exception {

        httpSecurity.oauth2ResourceServer(configurer -> {
            configurer.jwt(Customizer.withDefaults());
        });

        httpSecurity.authorizeHttpRequests(requestRegistry -> {
            requestRegistry.requestMatchers("/api/v1/admin/accounts/keycloak")
                    .hasRole(SERVICE_ROLE);
            requestRegistry.requestMatchers("/api/v1/admin/accounts")
                    .hasRole(ADMIN_ROLE);
            requestRegistry.requestMatchers("/swagger-ui/*", "/v3/api-docs", "/v3/api-docs.yaml", "/v3/api-docs/*")
                    .permitAll();
            requestRegistry.requestMatchers("/actuator/health")
                    .permitAll();
            requestRegistry.anyRequest()
                    .authenticated();
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.addFilterBefore(gatewayHeadersFilter, AnonymousAuthenticationFilter.class);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

}
