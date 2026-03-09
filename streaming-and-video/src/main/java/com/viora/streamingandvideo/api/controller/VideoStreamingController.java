package com.viora.streamingandvideo.api.controller;

import com.viora.streamingandvideo.api.docs.StreamingApi;
import com.viora.streamingandvideo.domain.ports.in.GetMovieUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342",
        allowedHeaders = {"Authorization", "Range", "Content-Type"},
        methods = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD})
@RestController
@RequiredArgsConstructor
public class VideoStreamingController implements StreamingApi {

    private final GetMovieUseCase getMovieUseCase;

    public ResponseEntity<Resource> getPlaylist(String id) {
        Resource resource = getMovieUseCase.getMoviePlayback(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"))
                .body(resource);
    }

    public ResponseEntity<Resource> getSegment(String id, Long segment) {
        Resource resource = getMovieUseCase.getMovieSegment(id, segment);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp2t"))
                .body(resource);
    }


}
