package com.viora.streamingandvideo.api.controller;

import com.viora.streamingandvideo.domain.model.Movie;
import com.viora.streamingandvideo.domain.ports.in.SaveMovieUseCase;
import com.viora.streamingandvideo.domain.ports.in.command.SaveMovieCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoSavingController {

    private final SaveMovieUseCase saveMovieUseCase;

    @PostMapping("/{imdbId}")
    public ResponseEntity<Void> saveVideo(@PathVariable String imdbId, @RequestParam("video") MultipartFile video) {
        SaveMovieCommand saveMovieCommand = new SaveMovieCommand(imdbId, video);
        Movie movie = saveMovieUseCase.saveMovie(saveMovieCommand);
        return ResponseEntity.created(URI.create("/api/v1/streaming/movies/%s/index.m3u8".formatted(movie.getId())))
                .build();
    }




}
