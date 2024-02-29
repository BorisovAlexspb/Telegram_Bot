package edu.java.client.stackoverflow;

import jakarta.validation.constraints.NotNull;
import model.stackoverflow.QuestionsResponse;

public interface StackOverflowClient {
    QuestionsResponse getLastModificationTime(@NotNull long questionId);
}
