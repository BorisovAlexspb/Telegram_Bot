package edu.java.configuration;

import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubWebClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Configuration
public class ClientConfiguration {
    @Bean
    public GitHubClient gitHubClient(ExchangeFilterFunction filterFunction) {
        return new GitHubWebClient(filterFunction);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(ExchangeFilterFunction filterFunction) {
        return new StackOverflowWebClient(filterFunction);
    }

}
