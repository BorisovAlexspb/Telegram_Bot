package edu.java.client.github;

import edu.java.dto.entity.UpdateInfo;
import edu.java.dto.github.EventResponse;
import edu.java.dto.github.RepositoryResponse;
import edu.java.model.Link;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import static edu.java.dto.github.EventType.UNKNOWN;

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
    public List<EventResponse> fetchEvents(String owner, String repository) {
        return Arrays.stream(Objects.requireNonNull(webClient.get()
            .uri("/repos/{owner}/{repo}/events", owner, repository)
            .retrieve()
            .bodyToMono(EventResponse[].class)
            .block())).toList();
    }

    @Override
    public UpdateInfo checkForUpdate(Link link) {
        try {
            URI uri = new URI(link.getUrl());
            String[] pathParts = uri.getPath().split("/");
            var owner = pathParts[1];
            var repository = pathParts[2];
            RepositoryResponse response = getLastUpdateTime(owner, repository);
            if (response.pushedAt().isAfter(link.getUpdatedAt())) {
                EventResponse lastEvent = fetchEvents(owner, repository)
                    .stream()
                    .max(Comparator.comparing(EventResponse::createdAt))
                    .orElse(null);
                if (lastEvent != null && lastEvent.createdAt().isAfter(link.getUpdatedAt())) {
                    return new UpdateInfo(
                        true,
                        lastEvent.createdAt(),
                        lastEvent.type().generateUpdateMessage(lastEvent.payload())
                    );
                }
                return new UpdateInfo(
                    true,
                    response.pushedAt(),
                    UNKNOWN.generateUpdateMessage(null)
                );
            }
            var isNewUpdate = response.updatedAt().isAfter(link.getUpdatedAt());
            return new UpdateInfo(
                isNewUpdate,
                response.updatedAt(),
                isNewUpdate ?  UNKNOWN.generateUpdateMessage(null) : "Обновлений нет"
            );
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Link url is invalid (Could not parse to URI)" + link.getUrl(), e);
        }
    }

}
