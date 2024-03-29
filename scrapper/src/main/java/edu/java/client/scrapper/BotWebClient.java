package edu.java.client.scrapper;

import edu.java.dto.bot.ApiErrorResponse;
import edu.java.dto.bot.LinkUpdateRequest;
import edu.java.exception.ApiErrorException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotWebClient implements BotClient {
    private final WebClient webClient;

    public BotWebClient() {
        this.webClient = WebClient.create();
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
