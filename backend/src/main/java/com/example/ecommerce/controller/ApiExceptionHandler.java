package com.example.ecommerce.controller;

import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst().map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Invalid request.");
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
}
