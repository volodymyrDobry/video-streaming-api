package com.viora.streamingandvideo.api.activemq.events;

public record UserHistoryFailedEvent(
        String userId,
        String movieId,
        Long segment,
        String reason
) {
}
