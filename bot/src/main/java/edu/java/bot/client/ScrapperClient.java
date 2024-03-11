package edu.java.bot.client;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.ApiErrorResponse;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.ListLinkResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import edu.java.bot.exception.ApiException;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final WebClient webClient;

    private final static String PATH_TO_CHAT = "tg-chat/{id}";
    private final static String PATH_TO_LINK = "/links";
    private final static String HEADER_NAME = "Tg-Chat-Id";

    public ScrapperClient(String baseUrl) {
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
                    .bodyToMono(ApiErrorResponse.class)
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
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiException(errorResponse)))
            )
            .bodyToMono(String.class)
            .blockOptional();
    }

    public Optional<ListLinkResponse> getLinks(Long id) {
        return webClient
            .get()
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiException(errorResponse)))
            )
            .bodyToMono(ListLinkResponse.class)
            .blockOptional();
    }

    public Optional<LinkResponse> addLink(Long id, AddLinkRequest request) {
        return webClient
            .post()
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiException(errorResponse)))
            )
            .bodyToMono(LinkResponse.class)
            .blockOptional();
    }

    public Optional<LinkResponse> removeLink(Long id, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiException(errorResponse)))
            )
            .bodyToMono(LinkResponse.class)
            .blockOptional();
    }

}
