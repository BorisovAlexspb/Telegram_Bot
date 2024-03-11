package edu.java.exception;

import edu.java.dto.bot.ApiErrorResponse;
import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public ApiErrorException(ApiErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
