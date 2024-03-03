package edu.java.exception;

import edu.java.dto.bot.ApiErrorResponce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ScrapperControllerExceptionHandler {

    private static final String BAD_ERROR = "400";
    private static final String NOT_FOUND = "404";

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ApiErrorResponce> handleBadRequestException(BadRequestException e) {
        ApiErrorResponce responce = new ApiErrorResponce("Bad request", BAD_ERROR, e);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(responce);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ApiErrorResponce> handleNotFoundException(NotFoundException e) {
        ApiErrorResponce responce = new ApiErrorResponce("Not found", NOT_FOUND, e);
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(responce);
    }
}
