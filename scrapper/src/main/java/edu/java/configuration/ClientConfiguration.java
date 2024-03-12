package edu.java.configuration;

import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubWebClient;
import edu.java.client.scrapper.BotClient;
import edu.java.client.scrapper.BotWebClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubWebClient();
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowWebClient();
    }

    @Bean
    public BotClient botClient() {
        return new BotWebClient();
    }
}
