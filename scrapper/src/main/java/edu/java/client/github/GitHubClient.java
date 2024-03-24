package edu.java.client.github;

import edu.java.dto.entity.jdbc.Link;
import edu.java.dto.entity.jdbc.UpdateInfo;
import edu.java.dto.github.EventResponse;
import edu.java.dto.github.RepositoryResponse;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface GitHubClient {
    RepositoryResponse getLastUpdateTime(@NotNull String repositoryOwner, @NotNull String repositoryName);

    List<EventResponse> fetchEvents(String owner, String repository);

    UpdateInfo checkForUpdate(Link link);
}
