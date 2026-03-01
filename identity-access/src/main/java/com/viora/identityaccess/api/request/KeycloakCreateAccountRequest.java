package com.viora.identityaccess.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record KeycloakCreateAccountRequest(
        @NotEmpty
        String id,
        @NotEmpty
        @Email
        String email
) {
}
