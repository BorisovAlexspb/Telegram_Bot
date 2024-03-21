package edu.java.scrapper.domain.jooq;

import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.model.Chat;
import edu.java.scrapper.IntegrationTest;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static edu.java.domain.jooq.tables.Chat.CHAT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqChatRepositoryTest extends IntegrationTest {

    private static final Chat TEST_CHAT = new Chat(255L);

    @Autowired
    private JooqChatRepository chatRepository;

    @Autowired
    private DSLContext dslContext;

    @Test
    @Transactional
    @Rollback
    void saveChatTest() {
        chatRepository.add(TEST_CHAT.getId());

        var chats = getChats();

        assertThat(chats).isNotEmpty();
        assertThat(chats).hasSize(1);
        assertThat(chats.getFirst().getId()).isEqualTo(TEST_CHAT.getId());
    }

    @Test
    @Transactional
    @Rollback
    void deleteChat() {
        chatRepository.add(TEST_CHAT.getId());

        chatRepository.remove(TEST_CHAT.getId());

        var chats = getChats();

        assertThat(chats).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findById() {
        var chat = chatRepository.find(TEST_CHAT.getId());
        assertThat(chat).isEqualTo(Optional.empty());

        chatRepository.add(TEST_CHAT.getId());

        chat = chatRepository.find(TEST_CHAT.getId());
        assertThat(chat).isNotNull();
    }

    private List<Chat> getChats() {
        return dslContext.select(CHAT.fields())
                .from(CHAT)
                .fetchInto(Chat.class);
    }
}
