package edu.java.client;

import model.stackoverflow.QuestionsResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface StackOverflowClient {
    @GetExchange("/2.3/questions/{questionId}?order=desc&sort=activity&site=stackoverflow")
    QuestionsResponse getLastModificationTime(@PathVariable long questionId);
}
