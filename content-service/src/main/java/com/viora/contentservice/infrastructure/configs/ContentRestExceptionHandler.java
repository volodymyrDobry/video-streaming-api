package com.viora.contentservice.infrastructure.configs;

import com.viora.contentservice.domain.exception.ActorNotFoundException;
import com.viora.contentservice.domain.exception.DomainValidationException;
import com.viora.contentservice.domain.exception.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ContentRestExceptionHandler {

    @ExceptionHandler({ActorNotFoundException.class, MovieNotFoundException.class})
    public ResponseEntity<Error> handleActorNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Error(LocalDateTime.now(), exception.getMessage()));
    }

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<?> handleDomainValidationExceptionHandler(DomainValidationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Error(LocalDateTime.now(), exception.getMessage()));
    }

    public record Error(
            LocalDateTime timeStamp,
            String description
    ) {
    }
}
