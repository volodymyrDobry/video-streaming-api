package com.viora.contentservice.domain.vo;

import com.viora.contentservice.domain.domain.Poster;

public record MovieSummary(
        String name,
        Poster poster
) {
}
