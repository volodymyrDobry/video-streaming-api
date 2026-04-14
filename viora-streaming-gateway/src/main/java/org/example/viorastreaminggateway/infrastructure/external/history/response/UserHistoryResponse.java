package org.example.viorastreaminggateway.infrastructure.external.history.response;

public record UserHistoryResponse(
        String id,
        String userId,
        String movieId,
        Long segment
) {
}
