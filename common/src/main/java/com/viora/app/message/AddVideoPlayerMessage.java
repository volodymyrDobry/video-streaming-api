package com.viora.app.message;

public record AddVideoPlayerMessage(
        String imdbId,
        String playbackUrl
) {
}
