package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.QuestionsResponse;
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
}
