package edu.java.scrapper.domain.jdbc;

import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.dto.entity.jdbc.Chat;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcChatRepositoryTest extends IntegrationTest {

    private static final Chat CHAT = new Chat(255L);

    @Autowired
    private JdbcChatRepository chatRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void saveChatTest() {
        chatRepository.add(CHAT.getId());

        var chats = getChats();

        assertThat(chats).isNotEmpty();
        assertThat(chats).hasSize(1);
        assertThat(chats.getFirst().getId()).isEqualTo(CHAT.getId());
    }

    @Test
    @Transactional
    @Rollback
    void deleteChat() {
        chatRepository.add(CHAT.getId());

        chatRepository.remove(CHAT.getId());

        var chats = getChats();

        assertThat(chats).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findById() {
        var chat = chatRepository.find(CHAT.getId());
        assertThat(chat).isEqualTo(Optional.empty());

        chatRepository.add(CHAT.getId());

        chat = chatRepository.find(CHAT.getId());
        assertThat(chat).isNotNull();
    }

    private List<Chat> getChats() {
        return jdbcTemplate.query(
            "SELECT ID FROM chat",
            (rs, rowNum) -> new Chat(rs.getLong("ID"))
        );
    }
}
