package com.viora.streamingandvideo.api.controller;

import com.viora.streamingandvideo.infrastructure.external.MovieDetailsClient;
import com.viora.streamingandvideo.infrastructure.ports.MovieDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/video/tests")
@RequiredArgsConstructor
public class VideoResilience4jController {

    private final MovieDetailsClient movieDetailsClient;

    @SneakyThrows
    @GetMapping("/get-video-details")
    public ResponseEntity<Void> getVideoDetails() {
        movieDetailsClient.getMovieDetailsByImdbId("tt0371746").get();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save-movie")
    public ResponseEntity<Void> saveMovie() {
        URI uri = URI.create("/api/v1/streaming/movies/%s/index.m3u8".formatted("tt0371746"));
        movieDetailsClient.saveMovie("69ad9a60379de10efece1dcc", uri);
        return ResponseEntity.ok().build();
    }

}
