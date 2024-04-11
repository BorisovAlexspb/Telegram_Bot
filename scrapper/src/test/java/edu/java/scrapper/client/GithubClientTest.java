package edu.java.scrapper.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubWebClient;
import edu.java.dto.github.RepositoryResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
public class GithubClientTest {

    @Autowired
    private ExchangeFilterFunction filterFunction;

    private WireMockServer wireMockServer = new WireMockServer();
    GitHubClient client = new GitHubWebClient("http://localhost:8080",filterFunction);
    private static final String TEST_JSON = "{\"updated_at\": \"2023-08-01T19:20:49Z\"," +
        "\"name\": \"BorisovAlexspb\"}";

    @Test
    public void test() {
        wireMockServer.start();

        stubFor(get("/repos/BorisovAlexspb/Tinkoff_Course_Spring").willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(TEST_JSON)));

        OffsetDateTime expected = OffsetDateTime.parse("2023-08-01T19:20:49Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        RepositoryResponse response = client.getLastUpdateTime("BorisovAlexspb", "Tinkoff_Course_Spring");

        assertThat(response.updatedAt()).isEqualTo(expected);
        wireMockServer.stop();
    }
}
