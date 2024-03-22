package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.QuestionsResponse;
import edu.java.model.Link;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
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
    public OffsetDateTime checkForUpdate(Link link) {
        try {
            var uri = new URI(link.getUrl());
            String[] pathParts = uri.getPath().split("/");
            Long questionId = Long.parseLong(pathParts[pathParts.length - 2]);

            QuestionsResponse response = getLastModificationTime(questionId);
            return response.items().getFirst().lastActivityDate();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Link url is invalid (Could not parse to URI)" + link.getUrl(), e);
        }

    }
}
