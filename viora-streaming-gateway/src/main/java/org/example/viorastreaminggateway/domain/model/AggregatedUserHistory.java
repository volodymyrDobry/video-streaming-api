package org.example.viorastreaminggateway.domain.model;

import lombok.Builder;

@Builder
public record AggregatedUserHistory(
        String movieId,
        Long segment,
        String movieTitle,
        String moviePoster
) {
}
