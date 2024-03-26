package edu.java.client.stackoverflow;

import edu.java.dto.entity.jdbc.UpdateInfo;
import edu.java.dto.stackoverflow.QuestionsResponse;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public interface StackOverflowClient {
    QuestionsResponse getLastModificationTime(@NotNull long questionId);

    UpdateInfo checkForUpdate(String url, OffsetDateTime lastUpdated, int answerCount);

    Long getQuestionId(String url);
}
