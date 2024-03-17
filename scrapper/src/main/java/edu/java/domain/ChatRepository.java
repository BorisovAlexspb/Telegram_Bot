package edu.java.domain;

import edu.java.model.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    Integer add(Long chatId);

    Integer remove(Long chatId);

    List<Chat> findAll();

    Optional<Chat> find(Long chatId);
}
