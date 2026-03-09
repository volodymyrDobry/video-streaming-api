package com.viora.streamingandvideo.infrastructure.configs;

import com.viora.streamingandvideo.domain.exception.MovieAlreadyExistsException;
import com.viora.streamingandvideo.domain.exception.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class MovieRestExceptionHandler {

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Error> handleMovieNotFoundException(MovieNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Error(LocalDateTime.now(), exception.getMessage()));
    }

    @ExceptionHandler(MovieAlreadyExistsException.class)
    public ResponseEntity<Error> handleMovieAlreadyExistsException(MovieNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new Error(LocalDateTime.now(), exception.getMessage()));
    }


    public record Error(
            LocalDateTime timeStamp,
            String description
    ) {
    }

}
