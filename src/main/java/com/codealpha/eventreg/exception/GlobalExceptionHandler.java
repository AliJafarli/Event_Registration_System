package com.codealpha.eventreg.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.*;

import static com.codealpha.eventreg.exception.ApiExceptions.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(
            OffsetDateTime timestamp,
            int status,
            String error,
            String message,
            String path,
            Map<String, Object> details
    ) {}

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ErrorResponse> notFound(NotFound ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler({BadRequest.class})
    public ResponseEntity<ErrorResponse> badRequest(RuntimeException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(Conflict.class)
    public ResponseEntity<ErrorResponse> conflict(Conflict ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(Forbidden.class)
    public ResponseEntity<ErrorResponse> forbidden(Forbidden ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, Object> details = new LinkedHashMap<>();
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));
        details.put("fieldErrors", fieldErrors);
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", req.getRequestURI(), details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generic(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req.getRequestURI(),
                Map.of("exception", ex.getClass().getSimpleName()));
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, String path, Map<String, Object> details) {
        return ResponseEntity.status(status).body(new ErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                details == null ? Map.of() : details
        ));
    }
}
