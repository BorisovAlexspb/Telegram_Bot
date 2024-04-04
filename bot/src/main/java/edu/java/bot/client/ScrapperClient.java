package edu.java.bot.client;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.ListLinkResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ScrapperClient {
    private final WebClient webClient;

    private final static String PATH_TO_CHAT = "/tg-chat/{id}";
    private final static String PATH_TO_LINK = "/links";
    private final static String HEADER_NAME = "Tg-Chat-Id";

    public ScrapperClient(@Value("${scrapper.base-url}") String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public String registerChat(Long id) {
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(PATH_TO_CHAT).build(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> Mono.empty()
            )
            .bodyToMono(String.class)
            .block();
    }

    public String deleteChat(Long id) {
        return webClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path(PATH_TO_CHAT).build(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> Mono.empty()
            )
            .bodyToMono(String.class)
            .block();
    }

    public ListLinkResponse getLinks(Long id) {
        return webClient
            .get()
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .retrieve()
            .bodyToMono(ListLinkResponse.class)
            .block();
    }

    public LinkResponse addLink(Long id, AddLinkRequest request) {
        return webClient
            .post()
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> Mono.empty()
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse removeLink(Long id, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
            .uri(PATH_TO_LINK)
            .header(HEADER_NAME, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> Mono.empty()
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }

}
