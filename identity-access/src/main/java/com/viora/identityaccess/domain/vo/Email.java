package com.viora.identityaccess.domain.vo;

import java.util.Objects;

public record Email(
        String email
) {

    private static final String REGEX = "[^@]+@[^@]+\\.[^@]+";

    public Email {
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (!email.matches(REGEX)) {
            throw new IllegalArgumentException("Email does not follow standard user@example.com");
        }
    }
}
