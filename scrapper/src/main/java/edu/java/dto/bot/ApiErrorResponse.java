package edu.java.dto.bot;

import java.util.Arrays;
import java.util.List;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    List<String> stacktrace
) {
    public ApiErrorResponse(String description, String code, Exception e) {
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
