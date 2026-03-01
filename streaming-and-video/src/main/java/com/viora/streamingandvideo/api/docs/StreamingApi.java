package com.viora.streamingandvideo.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Streaming", description = "HLS video streaming endpoints")
@RequestMapping("/api/v1/streaming")
public interface StreamingApi {

    @Operation(
            summary = "Get HLS playlist",
            description = "Returns .m3u8 playlist file used for adaptive video streaming",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Playlist returned",
                            content = @Content(mediaType = "application/vnd.apple.mpegurl")
                    ),
                    @ApiResponse(responseCode = "404", description = "Movie not found")
            }
    )
    @GetMapping("/movies/{id}/index.m3u8")
    ResponseEntity<Resource> getPlaylist(
            @Parameter(description = "Movie ID", example = "1")
            @PathVariable Long id
    );

    @Operation(
            summary = "Get HLS segment",
            description = "Returns a .ts video segment for streaming playback",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Segment returned",
                            content = @Content(mediaType = "video/mp2t")
                    ),
                    @ApiResponse(responseCode = "404", description = "Segment not found")
            }
    )
    @GetMapping("/movies/{id}/segment_{segment}.ts")
    ResponseEntity<Resource> getSegment(
            @Parameter(description = "Movie ID", example = "1")
            @PathVariable Long id,

            @Parameter(description = "Segment number", example = "1")
            @PathVariable Long segment
    );
}
