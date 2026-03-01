package com.viora.identityaccess.infrastructure.configs;

import com.viora.identityaccess.domain.exception.AlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Error> handleAlreadyExistsException(AlreadyExistsException existsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new Error(LocalDateTime.now(), existsException.getMessage()));
    }


    public record Error(
            LocalDateTime timeStamp,
            String errorMessage
    ) {
    }
}
