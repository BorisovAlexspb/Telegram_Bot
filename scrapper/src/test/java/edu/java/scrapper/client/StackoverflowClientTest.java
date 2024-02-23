package edu.java.scrapper.client;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.StackOverflowClient;
import edu.java.configuration.ClientConfiguration;
import model.stackoverflow.QuestionResponse;
import model.stackoverflow.QuestionsResponse;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StackoverflowClientTest {

    private WireMockServer wireMockServer = new WireMockServer();
    private final ClientConfiguration configuration = new ClientConfiguration();

    @Test
    public void test() {
        wireMockServer.start();

        stubFor(get("/2.3/questions/123?order=desc&sort=activity&site=stackoverflow").willReturn(okJson(
            """
                {
                    "items": [
                        {
                             "last_activity_date": 1594829479
                        }
                    ]
                }
                 """
        )));

        StackOverflowClient client = configuration.stackOverflowClient("http://localhost:8080");
        OffsetDateTime expected = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1594829479), ZoneOffset.UTC);

        QuestionsResponse response = client.getLastModificationTime(123);

        assertThat(response.items()).singleElement().isEqualTo(new QuestionResponse(expected));
        wireMockServer.stop();
    }
}
