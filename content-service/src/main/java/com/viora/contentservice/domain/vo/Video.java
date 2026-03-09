package com.viora.contentservice.domain.vo;

import com.viora.contentservice.domain.exception.DomainValidationException;

public record Video(
        String playerUrl
) {

    public Video {
        if (playerUrl == null || playerUrl.isBlank()) {
            throw new DomainValidationException("Player URL cannot be null or blank");
        }
    }

}
