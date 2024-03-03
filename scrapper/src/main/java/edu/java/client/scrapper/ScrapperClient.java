package edu.java.client.scrapper;

import edu.java.dto.bot.LinkUpdateRequest;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestBody;

public interface ScrapperClient {
    Optional<String> sendUpdate(@RequestBody LinkUpdateRequest request);
}
