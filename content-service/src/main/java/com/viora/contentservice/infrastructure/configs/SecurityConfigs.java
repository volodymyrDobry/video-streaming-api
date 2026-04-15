package com.viora.contentservice.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigs {
    @Bean
    public SecurityFilterChain httpSecurity(
            HttpSecurity httpSecurity,
            GatewayHeadersFilter gatewayHeadersFilter
    ) throws
            Exception {

        httpSecurity.authorizeHttpRequests(requestRegistry -> {
            requestRegistry.requestMatchers(HttpMethod.OPTIONS)
                    .permitAll();
            requestRegistry.requestMatchers("/swagger-ui/*", "/v3/api-docs", "/v3/api-docs.yaml", "/v3/api-docs/*")
                    .permitAll();
            requestRegistry.requestMatchers("/actuator/health")
                    .permitAll();
            requestRegistry.anyRequest()
                    .authenticated();
        });

        httpSecurity.addFilterBefore(gatewayHeadersFilter, AnonymousAuthenticationFilter.class);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        // TODO read how to configure csrf for microservices, for now we disable it since we don't have any session
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

}
