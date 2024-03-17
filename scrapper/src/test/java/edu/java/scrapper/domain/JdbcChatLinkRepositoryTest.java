package edu.java.scrapper.domain;

import edu.java.domain.jdbc.JdbcChatLinkRepository;
import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.model.Chat;
import edu.java.model.ChatLink;
import edu.java.model.Link;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.Test;
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
        null, null
    );

    private static final Link SECOND_LINK = new Link(
        2, "github.com/dummy/dummy2_repo",
        null, null
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

    private void saveLinkWithId(Link link) {
        jdbcTemplate.update("""
                INSERT INTO link
                    (ID, URL)
                    VALUES (?, ?)
            """, link.getId(), link.getUrl());
    }

    private List<ChatLink> getChatLinks() {
        return jdbcTemplate.query(
            "SELECT id, Chat_id, Link_id FROM chat_link",
            (rs, rowNum) -> new ChatLink(
                rs.getInt("chat_id"),
                rs.getLong("link_id")
            )
        );
    }
}
