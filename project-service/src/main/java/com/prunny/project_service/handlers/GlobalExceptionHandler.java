package com.prunny.project_service.handlers;

import com.prunny.project_service.dto.responses.ApiResponse;
import com.prunny.project_service.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException exception,
                                                                   HttpServletRequest request) {
        log(exception.getMessage());
        ApiResponse<?> response =
                buildApiResponse("BadRequest", "Invalid user input", request.getRequestURI());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        log(exception.getMessage());
        ApiResponse<?> response =
                buildApiResponse("ResourceNotFound", exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(NOT_FOUND).body(response);
    }

    private static ApiResponse<?> buildApiResponse(String error, String message, String path) {
        return ApiResponse.builder()
                .responseTime(now())
                .success(false)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }

    private static void log(String exceptionMessage) {
        log.error("ERROR: {}", exceptionMessage);
    }

}
