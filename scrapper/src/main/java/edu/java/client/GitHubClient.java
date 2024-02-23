package edu.java.client;

import model.github.RepositoryResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface GitHubClient {
    @GetExchange("/repos/{repositoryOwner}/{repositoryName}")
    RepositoryResponse getLastUpdateTime(@PathVariable String repositoryOwner, @PathVariable String repositoryName);
}
