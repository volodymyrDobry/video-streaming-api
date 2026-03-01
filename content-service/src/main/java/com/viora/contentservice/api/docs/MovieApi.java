package com.viora.contentservice.api.docs;

import com.viora.contentservice.api.request.AddMovieRequest;
import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.vo.MovieSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@Tag(name = "Movies", description = "Operations related to movies")
@RequestMapping("/api/v1/movies")
public interface MovieApi {

    @Operation(
            summary = "Add a new movie",
            description = "Creates a new movie with optional video file upload",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Movie successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> addMovie(

            @Parameter(
                    description = "Movie metadata",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AddMovieRequest.class)
                    )
            )
            @Valid @RequestPart("details") AddMovieRequest details,

            @Parameter(
                    description = "Movie file (optional video file)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart(value = "movie", required = false) MultipartFile file
    );

    @Operation(
            summary = "Get movie by ID",
            description = "Returns full movie details by its ID"
    )
    @ApiResponse(responseCode = "200", description = "Movie found")
    @ApiResponse(responseCode = "404", description = "Movie not found")
    @GetMapping("/{id}")
    ResponseEntity<MovieDetails> getMovieById(
            @Parameter(description = "Movie ID", example = "1")
            @PathVariable Long id
    );

    @Operation(
            summary = "Search movies by name",
            description = "Returns list of movies matching provided name"
    )
    @ApiResponse(responseCode = "200", description = "Movies retrieved")
    @GetMapping
    ResponseEntity<Collection<MovieSummary>> getMoviesByName(
            @Parameter(description = "Movie name to search", example = "Spider-Man")
            @RequestParam(required = false) String name
    );
}
