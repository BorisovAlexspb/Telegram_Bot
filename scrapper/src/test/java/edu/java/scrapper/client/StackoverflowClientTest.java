package edu.java.scrapper.client;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowWebClient;
import edu.java.dto.stackoverflow.QuestionResponse;
import edu.java.dto.stackoverflow.QuestionsResponse;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StackoverflowClientTest {

    private WireMockServer wireMockServer = new WireMockServer(8080);
    private final StackOverflowClient client = new StackOverflowWebClient("http://localhost:8080");
    @Test
    public void test() {
        wireMockServer.start();

        stubFor(get("/questions/123").willReturn(okJson(
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

        OffsetDateTime expected = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1594829479), ZoneOffset.UTC);

        QuestionsResponse response = client.getLastModificationTime(123);

        assertThat(response.items()).singleElement().isEqualTo(new QuestionResponse(expected));
        wireMockServer.stop();
    }
}
