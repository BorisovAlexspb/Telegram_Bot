package edu.java.domain;

import edu.java.model.Chat;
import java.util.Optional;

public interface ChatRepository {

    Integer add(Long chatId);

    Integer remove(Long chatId);

    Optional<Chat> find(Long chatId);
}
