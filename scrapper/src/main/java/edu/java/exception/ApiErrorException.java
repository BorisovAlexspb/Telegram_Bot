package edu.java.exception;

import edu.java.dto.bot.ApiErrorResponce;
import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException {
    private final ApiErrorResponce errorResponse;

    public ApiErrorException(ApiErrorResponce errorResponse) {
        this.errorResponse = errorResponse;
    }
}
