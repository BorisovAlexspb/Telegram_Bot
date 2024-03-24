package edu.java.domain.repository.jdbc;

import edu.java.domain.repository.ChatRepository;
import edu.java.model.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private final JdbcClient jdbcClient;

    @Override
    public void add(Long chatId) {
        jdbcClient.sql("INSERT INTO chat(ID) VALUES (?)")
                .param(chatId)
                .update();
    }

    @Override
    public void remove(Long chatId) {
        jdbcClient.sql("DELETE FROM chat WHERE ID = ?")
                .param(chatId)
                .update();
    }

    @Override
    public Chat find(Long chatId) {
        return jdbcClient.sql("SELECT ID FROM chat WHERE ID = ?")
                .param(chatId)
                .query(Chat.class)
                .single();
    }
}
