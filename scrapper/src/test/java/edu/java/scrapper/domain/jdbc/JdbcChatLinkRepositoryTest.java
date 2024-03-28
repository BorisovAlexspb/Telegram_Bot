package edu.java.scrapper.domain.jdbc;

import edu.java.domain.repository.jdbc.JdbcChatLinkRepository;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.dto.entity.jdbc.Chat;
import edu.java.dto.entity.jdbc.ChatLink;
import edu.java.dto.entity.jdbc.Link;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcChatLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcChatLinkRepository chatLinkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Link LINK = new Link(
        1, "github.com/dummy/dummy_repo",
        null, null,null
    );

    private static final Link SECOND_LINK = new Link(
        2, "github.com/dummy/dummy2_repo",
        null, null,null
    );

    private static final Chat CHAT = new Chat(255L);
    private static final Chat SECOND_CHAT = new Chat(100L);

    @Test
    @Transactional
    @Rollback
    public void addLinkToChatTest() {
        saveLinkWithId(LINK);
        chatRepository.add(CHAT.getId());
        chatLinkRepository.add(LINK.getId(), CHAT.getId());

        var chatLinks = getChatLinks();

        assertThat(chatLinks).hasSize(1);
        assertThat(chatLinks.getFirst().getChatId()).isEqualTo(CHAT.getId());
        assertThat(chatLinks.getFirst().getLinkId()).isEqualTo(LINK.getId());
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkFromChatTest() {
        chatRepository.add(CHAT.getId());
        saveLinkWithId(LINK);
        saveLinkWithId(SECOND_LINK);
        chatLinkRepository.add(LINK.getId(), CHAT.getId());
        chatLinkRepository.add(SECOND_LINK.getId(), CHAT.getId());

        chatLinkRepository.remove(CHAT.getId(), LINK);

        var chatLinks = getChatLinks();

        assertThat(chatLinks).hasSize(1);
        assertThat(chatLinks.getFirst().getChatId()).isEqualTo(CHAT.getId());
        assertThat(chatLinks.getFirst().getLinkId()).isEqualTo(SECOND_LINK.getId());
    }

    @Test
    @Transactional
    @Rollback
    void isLinkTrackedInChatTest() {
        chatRepository.add(CHAT.getId());
        saveLinkWithId(LINK);
        saveLinkWithId(SECOND_LINK);
        chatLinkRepository.add(LINK.getId(), CHAT.getId());

        boolean actualResult1 = chatLinkRepository.isLinkPresentInChat(LINK, CHAT.getId());

        assertThat(actualResult1).isTrue();

        boolean actualResult2 = chatLinkRepository.isLinkPresentInChat(SECOND_LINK, CHAT.getId());

        assertThat(actualResult2).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void isLinkTrackedTest() {
        chatRepository.add(CHAT.getId());
        saveLinkWithId(LINK);
        saveLinkWithId(SECOND_LINK);
        chatLinkRepository.add(LINK.getId(), CHAT.getId());

        boolean actualResult1 = chatLinkRepository.isLinkPresent(LINK);

        assertThat(actualResult1).isTrue();

        boolean actualResult2 = chatLinkRepository.isLinkPresent(SECOND_LINK);

        assertThat(actualResult2).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void findAllLinksByChatIdTest() {
        chatRepository.add(CHAT.getId());
        saveLinkWithId(LINK);
        saveLinkWithId(SECOND_LINK);

        List<Link> emptyLinkList = chatLinkRepository.findLinksByChat(CHAT.getId());

        assertThat(emptyLinkList).isEmpty();

        chatLinkRepository.add(LINK.getId(), CHAT.getId());
        chatLinkRepository.add(SECOND_LINK.getId(), CHAT.getId());

        List<Link> notEmptyLinkList = chatLinkRepository.findLinksByChat(CHAT.getId());

        assertThat(notEmptyLinkList).isNotEmpty();
        assertThat(notEmptyLinkList).hasSize(2);
        assertThat(notEmptyLinkList.getFirst().getUrl()).isEqualTo(LINK.getUrl());
        assertThat(notEmptyLinkList.getLast().getUrl()).isEqualTo(SECOND_LINK.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void findAllChatIdsByLinkIdTest() {
        var chats = List.of(CHAT, SECOND_CHAT);
        saveLinkWithId(LINK);
        for (Chat chat : chats) {
            chatRepository.add(chat.getId());
            chatLinkRepository.add(LINK.getId(), chat.getId());
        }

        List<Long> chatIds = chatLinkRepository.findAllChatIdsByLinkId(LINK.getId().longValue());

        assertThat(chatIds).hasSize(2);
        assertThat(chatIds).containsAll(chats.stream().map(Chat::getId).toList());
    }

    private void saveLinkWithId(Link link) {
        jdbcTemplate.update("""
                INSERT INTO link
                    (ID, URL)
                    VALUES (?, ?)
            """, link.getId(), link.getUrl());
    }

    private List<ChatLink> getChatLinks() {
        return jdbcTemplate.query(
            "SELECT Chat_id, Link_id FROM chat_link",
            (rs, rowNum) -> new ChatLink(
                rs.getInt("Link_id"),
                rs.getLong("Chat_id")
            )
        );
    }
}
