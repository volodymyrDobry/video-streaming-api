package com.viora.contentservice.infrastructure.external.command;

import org.springframework.web.multipart.MultipartFile;

public record SaveMovieVideoCommand(
        Long id,
        String name,
        MultipartFile movie
) {
}
