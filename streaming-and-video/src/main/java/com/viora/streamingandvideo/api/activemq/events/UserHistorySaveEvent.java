package com.viora.streamingandvideo.api.activemq.events;

public record UserHistorySaveEvent(
        String userId, String movieId, Long segment
) {
}
