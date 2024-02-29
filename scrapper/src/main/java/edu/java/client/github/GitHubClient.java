package edu.java.client.github;

import jakarta.validation.constraints.NotNull;
import model.github.RepositoryResponse;

public interface GitHubClient {
    RepositoryResponse getLastUpdateTime(@NotNull String repositoryOwner, @NotNull String repositoryName);
}
