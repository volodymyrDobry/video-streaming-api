package org.example.userstreaminghistory.api.activemq.message;

public record UserHistoryEvent(
        String userId, String movieId, Long segment
) {
}
