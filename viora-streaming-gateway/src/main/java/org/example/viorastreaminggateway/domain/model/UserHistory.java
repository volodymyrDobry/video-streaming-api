package org.example.viorastreaminggateway.domain.model;

public record UserHistory(
        String userId,
        String movieId,
        Long segment
) {
}
