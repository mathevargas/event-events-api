package com.sistema.eventsapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> tratar(ApiException e) {

        var corpo = Map.of(
                "erro", e.getMessage(),
                "status", e.getStatus().value(),
                "timestamp", LocalDateTime.now().toString()
        );

        return ResponseEntity.status(e.getStatus()).body(corpo);
    }
}
