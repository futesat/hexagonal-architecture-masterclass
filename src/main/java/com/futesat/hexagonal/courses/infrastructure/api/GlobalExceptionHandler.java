package com.futesat.hexagonal.courses.infrastructure.api;

import com.futesat.hexagonal.courses.application.find.CourseNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// Este componente "escucha" todas las excepciones que saltan en los controladores
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Capturamos IllegalArgumentException (que lanzan nuestros Value Objects)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleDomainException(IllegalArgumentException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Domain Error");
        body.put("message", ex.getMessage());

        // Devolvemos 400 BAD REQUEST en lugar de 500
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CourseNotFound.class)
    public ResponseEntity<Object> handleNotFound(CourseNotFound ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            org.springframework.web.bind.MethodArgumentNotValidException ex,
            org.springframework.http.HttpHeaders headers,
            org.springframework.http.HttpStatusCode status,
            org.springframework.web.context.request.WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation Error");
        body.put("details", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
