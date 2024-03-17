package edu.java.client.github;

import edu.java.dto.github.RepositoryResponse;
import edu.java.model.Link;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public interface GitHubClient {
    RepositoryResponse getLastUpdateTime(@NotNull String repositoryOwner, @NotNull String repositoryName);

    OffsetDateTime checkForUpdate(Link link);
}
