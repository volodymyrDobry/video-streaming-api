package com.viora.app.configs;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CommonRestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .body(getFieldErrorsMapFromBindingResult(exception.getBindingResult()));
    }

    private Map<String, String> getFieldErrorsMapFromBindingResult(
            final BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(
                        Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage,
                                (oldValue, newValue) -> oldValue,
                                HashMap::new));
    }

}
