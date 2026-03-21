package com.viora.contentservice.infrastructure.external.message;

public record StreamingAndVideoPayload(
        String imdbId,
        String playbackUrl
) {
}
