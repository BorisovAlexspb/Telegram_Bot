package edu.java.client.stackoverflow;

import edu.java.dto.entity.jdbc.UpdateInfo;
import edu.java.dto.stackoverflow.QuestionsResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowWebClient implements StackOverflowClient {

    @Value(value = "${api.stackoverflow.baseURL}")
    private String baseUrl;
    private final WebClient webClient;

    public StackOverflowWebClient(ExchangeFilterFunction filterFunction) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .filter(filterFunction)
            .build();
    }

    public StackOverflowWebClient(String url, ExchangeFilterFunction filterFunction) {
        this.webClient = WebClient.builder()
            .baseUrl(url)
            .filter(filterFunction)
            .build();
    }

    @Override
    public QuestionsResponse getLastModificationTime(long questionId) {
        return this.webClient
            .get()
            .uri("/questions/{questionId}", questionId)
            .retrieve()
            .bodyToMono(QuestionsResponse.class)
            .block();
    }

    @Override
    public UpdateInfo checkForUpdate(String url, OffsetDateTime lastUpdated, int answerCount) {
        QuestionsResponse response = getLastModificationTime(getQuestionId(url));
        var question = response.items().getFirst();
        if (question.lastActivityDate().isAfter(lastUpdated)) {
            if (question.answerCount() > answerCount) {
                return new UpdateInfo(true, question.lastActivityDate(), "Появился новый ответ");
            }
            return new UpdateInfo(true, question.lastActivityDate(), "Произошло обновление в вопросе");
        }
        return new UpdateInfo(false, question.lastActivityDate(), "Обновлений нет");

    }

    @Override
    public Long getQuestionId(String url) {
        try {
            var uri = new URI(url);
            String[] pathParts = uri.getPath().split("/");
            return Long.parseLong(pathParts[pathParts.length - 2]);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Link url is invalid (Could not parse to URI)" + url, e);
        }

    }
}
