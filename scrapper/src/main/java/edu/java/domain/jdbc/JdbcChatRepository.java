package edu.java.domain.jdbc;

import edu.java.domain.ChatRepository;
import edu.java.model.Chat;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private final JdbcClient jdbcClient;

    @Override
    public Integer add(Long chatId) {
        return jdbcClient.sql("INSERT INTO chat(ID) VALUES (?)")
            .param(chatId)
            .update();
    }

    @Override
    public Integer remove(Long chatId) {
        return jdbcClient.sql("DELETE FROM chat WHERE ID = ?")
            .param(chatId)
            .update();
    }

//    @Override
//    public List<Chat> findAll() {
//        return jdbcClient.sql("SELECT * FROM chat")
//            .query(Chat.class)
//            .list();
//    }

    @Override
    public Optional<Chat> find(Long chatId) {
        return jdbcClient.sql("SELECT ID FROM chat WHERE ID = ?")
            .param(chatId)
            .query(Chat.class)
            .optional();
    }
}
