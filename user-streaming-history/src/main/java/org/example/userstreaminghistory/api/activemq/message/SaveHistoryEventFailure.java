package org.example.userstreaminghistory.api.activemq.message;

import lombok.Builder;

@Builder
public record SaveHistoryEventFailure(
        String userId,
        String movieId,
        Long segment,
        String reason
) {
}
