package com.viora.streamingandvideo.domain.model;

public record MovieDetails(
        String id,
        String name,
        String imdbId,
        String playerUrl
) {
}
