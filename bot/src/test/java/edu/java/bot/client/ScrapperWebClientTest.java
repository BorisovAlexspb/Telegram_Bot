package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.bot.configuration.retry.RetryConfigProperties;
import java.util.ArrayList;
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
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext
public class ScrapperWebClientTest {
    private static final Long CHAT_ID = 1L;

    private static final String CHAT_URL = "/tg-chat/1";

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
    void getCorrectResponseInFetchRepositoryWithRetry() {
        String expectedResult = "Чат зарегистрирован";
        var retryAbleCode = new ArrayList<>(retryConfigProperties.codes()).getFirst();
        wireMockServer.stubFor(post(urlEqualTo(CHAT_URL))
            .inScenario("Retry scenario")
            .whenScenarioStateIs(STARTED)
            .willSetStateTo("Retry succeeded")
            .willReturn(aResponse()
                .withStatus(retryAbleCode)
                .withHeader("Content-Type", "text/plain")
            )
        );

        wireMockServer.stubFor(post(urlEqualTo(CHAT_URL))
            .inScenario("Retry scenario")
            .whenScenarioStateIs("Retry succeeded")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/plain")
                .withBody(expectedResult)
            )
        );

        ScrapperClient client = new ScrapperClient(wireMockServer.baseUrl(), filterFunction);
        var repositoryResponse = client.registerChat(CHAT_ID);

        assertThat(repositoryResponse).isEqualTo(expectedResult);
        wireMockServer.verify(2, postRequestedFor((urlEqualTo(CHAT_URL))));
    }

    @Test
    void getCorrectErrorResponseAfterMaxRetry() {
        var retryAbleCode = new ArrayList<>(retryConfigProperties.codes()).getFirst();
        wireMockServer.stubFor(post(urlEqualTo(CHAT_URL))
            .willReturn(aResponse()
                .withStatus(retryAbleCode)
                .withHeader("Content-Type", "text/plain"))
        );

        ScrapperClient client = new ScrapperClient(wireMockServer.baseUrl(), filterFunction);

        assertThrows(WebClientResponseException.class, () -> client.registerChat(CHAT_ID));

        wireMockServer.verify(retryConfigProperties.maxAttempts() + 1, postRequestedFor((urlEqualTo(CHAT_URL))));
    }
}
