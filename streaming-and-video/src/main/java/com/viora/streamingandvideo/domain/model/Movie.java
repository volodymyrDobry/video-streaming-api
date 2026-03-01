package com.viora.streamingandvideo.domain.model;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Getter
public class Movie {

    private final String id;
    private final String name;
    private final MultipartFile file;

    private Movie(String id, String name, MultipartFile file) {
        this.id = id;
        this.name = name;
        this.file = file;
    }

    public static Movie createMovie(String id, String name, MultipartFile file) {
        if (!Objects.equals(file.getContentType(), "video/mp4")) {
            throw new IllegalArgumentException("Only MP4 allowed");
        }
        return new Movie(id, name, file);
    }
}
