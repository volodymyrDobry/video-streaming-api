package com.viora.app.message;

public record UserHistorySaveEvent(
        String userId, String movieId, Long segment
) {
}
