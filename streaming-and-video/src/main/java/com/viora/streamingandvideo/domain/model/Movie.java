package com.viora.streamingandvideo.domain.model;

import lombok.Getter;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Getter
public class Movie {

    private final String id;
    private final Resource file;

    private Movie(String id, Resource file) {
        this.id = id;
        this.file = file;
    }

    public static Movie createMovie(String id, Resource file) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(file);

        return new Movie(id, file);
    }
}
