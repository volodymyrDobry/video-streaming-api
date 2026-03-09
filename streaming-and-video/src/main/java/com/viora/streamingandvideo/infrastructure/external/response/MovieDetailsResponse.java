package com.viora.streamingandvideo.infrastructure.external.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MovieDetailsResponse(
        String id,
        String name,
        String imdbId,
        String playerUrl
) {
}
