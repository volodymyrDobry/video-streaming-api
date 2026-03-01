package com.viora.contentservice.domain.port.in.command;

import lombok.Builder;

import java.util.Set;

@Builder
public record AddMovieCommand(
        String name,
        String plot,
        Set<Long> actorsIds,
        String posterLink
) {
}
