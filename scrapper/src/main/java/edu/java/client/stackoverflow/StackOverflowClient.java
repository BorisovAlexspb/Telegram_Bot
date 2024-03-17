package edu.java.client.stackoverflow;

import edu.java.dto.UpdateInfo;
import edu.java.dto.stackoverflow.QuestionsResponse;
import edu.java.model.Link;
import jakarta.validation.constraints.NotNull;

public interface StackOverflowClient {
    QuestionsResponse getLastModificationTime(@NotNull long questionId);

    UpdateInfo checkForUpdate(Link link, int answerCount);

    Long getQuestionId(String url);
}
