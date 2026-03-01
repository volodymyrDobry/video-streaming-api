package com.viora.identityaccess.domain.model;

import java.util.Objects;

public record Role(
        Long id,
        String name
) {
    public Role {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("Role cannot be neither null nor empty");
        }
    }
}
