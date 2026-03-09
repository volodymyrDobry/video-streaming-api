package com.viora.contentservice.api.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record AddMovieRequest(
        @NotEmpty
        String name,
        @NotEmpty
        String plot,
        @NotEmpty
        Set<String> actorsIds,
        @NotEmpty
        String posterLink,
        @NotEmpty
        String imdbId
) {
}
