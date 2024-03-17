package edu.java.client.github;

import edu.java.dto.UpdateInfo;
import edu.java.dto.github.EventResponse;
import edu.java.dto.github.RepositoryResponse;
import edu.java.model.Link;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface GitHubClient {
    RepositoryResponse getLastUpdateTime(@NotNull String repositoryOwner, @NotNull String repositoryName);

    List<EventResponse> fetchEvents(String owner, String repository);

    UpdateInfo checkForUpdate(Link link);
}
