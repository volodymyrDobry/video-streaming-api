package com.viora.contentservice.api.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record AddMovieRequest(
        @NotEmpty
        String name,
        @NotEmpty
        String plot,
        @NotEmpty
        Set<Long> actorsIds,
        @NotEmpty
        String posterLink
) {
}
