package edu.java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScrapperExceptionHandler {

    private static final String BAD_ERROR = "400";
    private static final String NOT_FOUND = "404";

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(HttpStatus.BAD_REQUEST.name());
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(HttpStatus.NOT_FOUND.name());
    }
}
