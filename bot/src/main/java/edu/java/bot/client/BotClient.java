package edu.java.bot.client;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.ApiErrorResponce;
import edu.java.bot.dto.LinkResponce;
import edu.java.bot.dto.ListLinkResponce;
import edu.java.bot.dto.RemoveLinkRequest;
import edu.java.bot.exception.ApiException;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    private final WebClient webClient;

    private final static String PATH_TO_CHAT = "tg-chat/{id}";
    private final static String PATH_TO_LINK = "/links";
    private final static String HEADER_NAME = "Tg-Chat-Id";

    public BotClient(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public Optional<String> registerChat(Long id) {
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(PATH_TO_CHAT).build(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponce.class)
                    .flatMap(errorResponse -> Mono.error(new ApiException(errorResponse)))
            )
            .bodyToMono(String.class)
            .blockOptional();
    }

    public Optional<String> deleteChat(Long id) {
        return webClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path(PATH_TO_CHAT).build(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponce.class)
                    .flatMap(errorResponse -> Mono.error(new ApiException(errorResponse)))
            )
            .bodyToMono(String.class)
            .blockOptional();
    }

    public Optional<ListLinkResponce> getLinks(Long id) {
        return webClient
            .get()
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponce.class)
                    .flatMap(errorResponse -> Mono.error(new ApiException(errorResponse)))
            )
            .bodyToMono(ListLinkResponce.class)
            .blockOptional();
    }

    public Optional<LinkResponce> addLink(Long id, AddLinkRequest request) {
        return webClient
            .post()
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponce.class)
                    .flatMap(errorResponse -> Mono.error(new ApiException(errorResponse)))
            )
            .bodyToMono(LinkResponce.class)
            .blockOptional();
    }

    public Optional<LinkResponce> removeLink(Long id, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponce.class)
                    .flatMap(errorResponse -> Mono.error(new ApiException(errorResponse)))
            )
            .bodyToMono(LinkResponce.class)
            .blockOptional();
    }

}
