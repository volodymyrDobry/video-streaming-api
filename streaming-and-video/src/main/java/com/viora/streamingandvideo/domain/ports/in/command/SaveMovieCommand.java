package com.viora.streamingandvideo.domain.ports.in.command;

import org.springframework.web.multipart.MultipartFile;

public record SaveMovieCommand(
        String id,
        String name,
        MultipartFile movie
) {
}
