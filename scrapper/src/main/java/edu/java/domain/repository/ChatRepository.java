package edu.java.domain.repository;

import edu.java.dto.entity.jdbc.Chat;

public interface ChatRepository {

    void add(Long chatId);

    void remove(Long chatId);

    Chat find(Long chatId);
}
