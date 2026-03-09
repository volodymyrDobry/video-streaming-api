package com.viora.contentservice.api.docs;

import com.viora.contentservice.api.request.AddActorRequest;
import com.viora.contentservice.domain.domain.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Actors", description = "Operations related to actors")
@RequestMapping("/api/v1/actors")
public interface ActorsApi {

    @Operation(
            summary = "Add a new actor",
            description = "Creates a new actor in the system"
    )
    @ApiResponse(responseCode = "201", description = "Actor successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @PostMapping
    ResponseEntity<Actor> addActor(
            @Valid @RequestBody AddActorRequest request
    );

    @Operation(
            summary = "Get all actors",
            description = "Retrieves all actors from the system"
    )
    @ApiResponse(responseCode = "200", description = "Actors retrieved successfully")
    @GetMapping
    ResponseEntity<Set<Actor>> getAllActors();
}
