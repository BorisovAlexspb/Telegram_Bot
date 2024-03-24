package edu.java.scrapper.domain.jooq;

import edu.java.domain.jooq.Tables;
import edu.java.domain.repository.jooq.JooqChatLinkRepository;
import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.model.Chat;
import edu.java.model.ChatLink;
import edu.java.model.Link;
import edu.java.scrapper.IntegrationTest;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static edu.java.domain.jooq.Tables.CHAT_LINK;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqChatLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JooqChatLinkRepository chatLinkRepository;

    @Autowired
    private JooqChatRepository chatRepository;

    @Autowired
    private DSLContext dslContext;

    private static final Link LINK = new Link(
            1, "github.com/dummy/dummy_repo",
            null, null, null
    );

    private static final Link SECOND_LINK = new Link(
            2, "github.com/dummy/dummy2_repo",
            null, null, null
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
        dslContext.insertInto(Tables.LINK)
                .set(Tables.LINK.ID, link.getId().longValue())
                .set(Tables.LINK.URL, link.getUrl())
                .execute();
    }

    private List<ChatLink> getChatLinks() {
        return dslContext.select(CHAT_LINK.fields())
                .from(CHAT_LINK)
                .fetchInto(ChatLink.class);
    }
}
