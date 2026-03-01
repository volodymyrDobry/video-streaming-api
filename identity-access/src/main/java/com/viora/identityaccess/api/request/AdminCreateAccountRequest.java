package com.viora.identityaccess.api.request;

import com.viora.identityaccess.infrastructure.service.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record AdminCreateAccountRequest(
        @Email
        @NotEmpty
        String email,
        @NotEmpty
        Set<ERole> roles,
        @NotEmpty
        String password
) {
}
