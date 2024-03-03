package edu.java.bot.dto;

import java.util.Arrays;
import java.util.List;

public record ApiErrorResponce(String description,
                               String code,
                               String exceptionName,
                               String exceptionMessage,
                               List<String> stacktrace) {

    public ApiErrorResponce(String description, String code, Exception e) {
        this(
            description,
            code,
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }
}
