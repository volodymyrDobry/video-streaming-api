package com.viora.streamingandvideo.infrastructure.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class GatewayHeadersFilter extends OncePerRequestFilter {

    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String USERNAME_HEADER = "X-User-Username";
    public static final String ROLES_HEADER = "X-User-Roles";

    public static final String USER_ID_ATTRIBUTE = "gateway.userId";
    public static final String USERNAME_ATTRIBUTE = "gateway.username";
    public static final String ROLES_ATTRIBUTE = "gateway.roles";
    public static final String AUTHENTICATION_SOURCE = "gateway-headers";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String userId = request.getHeader(USER_ID_HEADER);
        String username = request.getHeader(USERNAME_HEADER);
        String rolesHeader = request.getHeader(ROLES_HEADER);

        List<String> roles = rolesHeader == null || rolesHeader.isBlank()
                ? List.of()
                : Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .toList();

        request.setAttribute(USER_ID_ATTRIBUTE, userId);
        request.setAttribute(USERNAME_ATTRIBUTE, username);
        request.setAttribute(ROLES_ATTRIBUTE, roles);

        if (Objects.nonNull(userId) || Objects.nonNull(username)) {
            List<GrantedAuthority> authorities = roles.stream()
                    .map(this::toAuthority)
                    .map(GrantedAuthority.class::cast)
                    .toList();

            String principal = username == null || username.isBlank() ? userId : username;
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authorities
            );
            authentication.setDetails(Map.of(
                    USER_ID_ATTRIBUTE, userId,
                    USERNAME_ATTRIBUTE, username,
                    ROLES_ATTRIBUTE, roles,
                    "source", AUTHENTICATION_SOURCE
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        log.trace("Gateway headers parsed: userId={}, username={}, roles={}", userId, username, roles);
        filterChain.doFilter(request, response);
    }

    private SimpleGrantedAuthority toAuthority(String role) {
        if (role.startsWith("ROLE_")) {
            return new SimpleGrantedAuthority(role);
        }
        return new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
    }
}
