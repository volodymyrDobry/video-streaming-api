package com.viora.streamingandvideo.domain.ports.in.command;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public record SaveMovieCommand(
        String imdbId,
        MultipartFile movie
) {
    public SaveMovieCommand{
        if (!Objects.equals(movie.getContentType(), "video/mp4")) {
            throw new IllegalArgumentException("Only MP4 allowed");
        }
    }
}
