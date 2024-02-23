package edu.java.scrapper.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.GitHubClient;
import edu.java.configuration.ClientConfiguration;
import model.github.RepositoryResponse;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class GithubClientTest {

    private WireMockServer wireMockServer = new WireMockServer();
    private final ClientConfiguration configuration = new ClientConfiguration();
    private static final String TEST_JSON = "{\"updated_at\": \"2023-08-01T19:20:49Z\"," +
        "\"name\": \"BorisovAlexspb\"}";

    @Test
    public void test() {
        wireMockServer.start();

        stubFor(get("/repos/BorisovAlexspb/Tinkoff_Course_Spring").willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(TEST_JSON)));

        GitHubClient client = configuration.githubClient("http://localhost:8080");
        OffsetDateTime expected = OffsetDateTime.parse("2023-08-01T19:20:49Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        RepositoryResponse response = client.getLastUpdateTime("BorisovAlexspb", "Tinkoff_Course_Spring");

        assertThat(response.updatedAt()).isEqualTo(expected);
        wireMockServer.stop();
    }
}
