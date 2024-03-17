package edu.java.client.stackoverflow;

import edu.java.dto.UpdateInfo;
import edu.java.dto.stackoverflow.QuestionsResponse;
import edu.java.model.Link;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowWebClient implements StackOverflowClient {

    @Value(value = "${api.stackoverflow.baseURL}")
    private String baseUrl;
    private final WebClient webClient;

    public StackOverflowWebClient() {
        this.webClient = WebClient.create(baseUrl);
    }

    public StackOverflowWebClient(String url) {
        this.webClient = WebClient.create(url);
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
    public UpdateInfo checkForUpdate(Link link, int answerCount) {
        QuestionsResponse response = getLastModificationTime(getQuestionId(link.getUrl()));
        var question = response.items().getFirst();
        if (question.lastActivityDate().isAfter(link.getUpdatedAt())) {
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
