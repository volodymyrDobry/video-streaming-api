package org.example.viorastreaminggateway.infrastructure.configs.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

public class JwtHelpersUtils {

    private JwtHelpersUtils() {
    }

    public static List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null) {
            return List.of();
        }
        Object roles = realmAccess.get("roles");
        if (roles instanceof List<?> rawRoles) {
            return rawRoles.stream().map(String::valueOf).toList();
        }
        return List.of();
    }

    public static SimpleGrantedAuthority toAuthority(String role) {
        if (role.startsWith("ROLE_")) {
            return new SimpleGrantedAuthority(role);
        }
        return new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
    }

}
