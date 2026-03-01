package com.viora.identityaccess.domain.vo;

import java.util.Objects;

public record AccountId(
        String id
) {
    public AccountId {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }
}
