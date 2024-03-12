package edu.java.client.github;

import edu.java.dto.github.RepositoryResponse;
import jakarta.validation.constraints.NotNull;

public interface GitHubClient {
    RepositoryResponse getLastUpdateTime(@NotNull String repositoryOwner, @NotNull String repositoryName);
}
