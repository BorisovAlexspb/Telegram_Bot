package edu.java.client.github;

import jakarta.validation.constraints.NotNull;
import model.github.RepositoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubWebClient implements GitHubClient {

    @Value(value = "${api.github.baseURL}")
    private String baseUrl;
    private final WebClient webClient;

    public GitHubWebClient() {
        this.webClient = WebClient.create(baseUrl);
    }

    public GitHubWebClient(String url) {
        this.webClient = WebClient.create(url);
    }

    @Override
    public RepositoryResponse getLastUpdateTime(@NotNull String repositoryOwner, @NotNull String repositoryName) {
        return this.webClient
            .get()
            .uri("/repos/{owner}/{repository}", repositoryOwner, repositoryName)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .block();
    }
}
