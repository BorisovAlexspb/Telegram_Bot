package edu.java.client.github;

import edu.java.dto.github.RepositoryResponse;
import edu.java.model.Link;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
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

    @Override
    public OffsetDateTime checkForUpdate(Link link) {
        try {
            URI uri = new URI(link.getUrl());
            String[] pathParts = uri.getPath().split("/");

            RepositoryResponse response = getLastUpdateTime(pathParts[1], pathParts[2]);

            return response.updatedAt();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Link url is invalid (Could not parse to URI)" + link.getUrl(), e);
        }
    }

}
