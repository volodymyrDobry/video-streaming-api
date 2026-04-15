package com.viora.app.message;

public record UserHistoryFailedEvent(
        String userId,
        String movieId,
        Long segment,
        String reason
) {
}
