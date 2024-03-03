package edu.java.bot.exception;

import edu.java.bot.dto.ApiErrorResponce;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final String code;

    public ApiException(ApiErrorResponce response) {
        super(response.exceptionMessage());
        code = response.code();
    }
}

