package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.QuestionsResponse;
import jakarta.validation.constraints.NotNull;

public interface StackOverflowClient {
    QuestionsResponse getLastModificationTime(@NotNull long questionId);
}
