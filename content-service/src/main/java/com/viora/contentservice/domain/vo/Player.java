package com.viora.contentservice.domain.vo;

import com.viora.contentservice.domain.exception.DomainValidationException;
import org.apache.commons.lang3.StringUtils;

public record Player(
        String url
) {
    public Player {
        if (StringUtils.isEmpty(url) || url.matches("^https?://.*")) {
            throw new DomainValidationException("Player URL cannot be null or empty");
        }
    }
}
