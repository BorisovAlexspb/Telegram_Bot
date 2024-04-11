package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.bot.BotClient;
import edu.java.client.bot.BotWebClient;
import edu.java.configuration.retry.RetryConfigProperties;
import edu.java.dto.bot.LinkUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext
public class BotClientTest {

    private static final String URL = "/updates";

    private static WireMockServer wireMockServer;

    @Autowired
    private RetryConfigProperties retryConfigProperties;

    @Autowired
    private ExchangeFilterFunction filterFunction;

    @BeforeAll
    static void prepare() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
    }

    @BeforeEach
    void reset() {
        wireMockServer.resetAll();
    }

    @Test
    void getCorrectErrorResponseAfterMaxRetry() {
        var retryAbleCode = new ArrayList<>(retryConfigProperties.codes()).getFirst();
        wireMockServer.stubFor(post(urlEqualTo(URL))
            .willReturn(aResponse()
                .withStatus(retryAbleCode)
                .withHeader("Content-Type", "text/plain")
            )
        );

        BotClient client = new BotWebClient(wireMockServer.baseUrl(), filterFunction);

        assertThrows(WebClientResponseException.class, () -> client.sendUpdate(
            new LinkUpdateRequest(
                1L,
                "github.com",
                "dummy",
                List.of(1L, 2L)
            )
        ));

        wireMockServer.verify(retryConfigProperties.maxAttempts() + 1, postRequestedFor((urlEqualTo(URL))));
    }

    @AfterAll
    static void finish() {
        wireMockServer.stop();
    }
}
