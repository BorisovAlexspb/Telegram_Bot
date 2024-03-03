package edu.java.bot.exception;

import edu.java.bot.dto.ApiErrorResponce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BotControllerExceptionHandler {

    private static final String BAD_ERROR = "400";

    @ExceptionHandler(ChatNotFoundException.class)
    protected ResponseEntity<ApiErrorResponce> handleChatNotFoundException(ChatNotFoundException e) {
        ApiErrorResponce responce = new ApiErrorResponce("Chat not found", BAD_ERROR, e);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(responce);
    }
}
