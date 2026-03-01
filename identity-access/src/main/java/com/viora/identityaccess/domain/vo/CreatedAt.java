package com.viora.identityaccess.domain.vo;

import java.time.LocalDateTime;
import java.util.Objects;

public record CreatedAt(
        LocalDateTime cratedAt
) {

    public CreatedAt {
        if (Objects.isNull(cratedAt)) {
            throw new IllegalArgumentException("Creation date cannot be null");
        }
        if (cratedAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Creation date cannot be in the future");
        }
    }

}
