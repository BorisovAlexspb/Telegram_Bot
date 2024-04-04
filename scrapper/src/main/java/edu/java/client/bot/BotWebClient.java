package edu.java.client.bot;

import edu.java.dto.bot.ApiErrorResponse;
import edu.java.dto.bot.LinkUpdateRequest;
import edu.java.exception.ApiErrorException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BotWebClient implements BotClient {
    private final WebClient webClient;

    public BotWebClient(@Value("${bot.base-url}") String baseURL) {
        this.webClient = WebClient.create(baseURL);
    }

    @Override
    public Optional<String> sendUpdate(LinkUpdateRequest request) {
        return webClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(String.class)
            .blockOptional();
    }
}
