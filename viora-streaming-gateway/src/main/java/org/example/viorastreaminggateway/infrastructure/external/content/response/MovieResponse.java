package org.example.viorastreaminggateway.infrastructure.external.content.response;

public record MovieResponse(
        String id,
        String name,
        Poster poster
) {
}
