// Programmed by Ayaz Ciplak
package comp307.backend.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler - converts our custom runtime exceptions into
 * HTTP responses whose body is the plain exception message string.
 *
 * Declaring explicit @ExceptionHandler methods here bypasses spring boot propagation 
 * guardrails and gives us full control of the response body.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 400 - bad input (e.g. wrong password, email already registered). */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /** 404 - resource not found (e.g. email not registered). */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /** 401 - invalid / expired token. */
    @ExceptionHandler(AuthFailureException.class)
    public ResponseEntity<String> handleAuthFailure(AuthFailureException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
