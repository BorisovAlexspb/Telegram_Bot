package edu.java.client.bot;

import edu.java.dto.bot.LinkUpdateRequest;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestBody;

public interface BotClient {
    Optional<String> sendUpdate(@RequestBody LinkUpdateRequest request);
}
