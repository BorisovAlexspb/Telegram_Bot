package edu.java.bot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BotExceptionHandler {

    private static final String BAD_ERROR = "400";

    @ExceptionHandler(ChatNotFoundException.class)
    protected ResponseEntity<String> handleChatNotFoundException(ChatNotFoundException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(HttpStatus.BAD_REQUEST.name());
    }
}
