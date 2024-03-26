package edu.java.scrapper.domain.jpa;

import edu.java.domain.repository.jpa.JpaChatLinkRepository;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.dto.entity.jpa.Chat;
import edu.java.dto.entity.jpa.ChatLink;
import edu.java.dto.entity.jpa.GithubRepositoryLink;
import edu.java.dto.entity.jpa.Link;
import edu.java.dto.entity.jpa.StackOverflowQuestionLink;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JpaChatLinkRepositoryTest extends IntegrationTest {

    private static final Link GITHUB_LINK = GithubRepositoryLink.builder()
        .id(1L)
        .url("github.com/dummy/dummy_repo")
        .chatLinks(new ArrayList<>())
        .build();

    private static final Link STACKOVERFLOW_LINK = StackOverflowQuestionLink.builder()
        .id(2L)
        .url("https://stackoverflow.com/questions/123/dummy")
        .chatLinks(new ArrayList<>())
        .build();

    private static final Chat CHAT = Chat.builder()
        .id(255L)
        .createdAt(OffsetDateTime.now())
        .build();

    private static final Chat SECOND_CHAT = Chat.builder()
        .id(100L)
        .createdAt(OffsetDateTime.now())
        .build();

    @Autowired
    private JpaChatLinkRepository chatLinkRepository;

    @Autowired
    private JpaChatRepository chatRepository;

    @Autowired
    private JpaLinkRepository linkRepository;

//    @Test
//    @Transactional
//    @Rollback
//    void save() {
//        var savedLink = linkRepository.save(GITHUB_LINK);
//        var savedChat = chatRepository.save(CHAT);
//
//        ChatLink chatLink = new ChatLink();
//        chatLink.setChat(savedChat);
//        chatLink.setLink(savedLink);
//
//        var savedChatLink = chatLinkRepository.save(chatLink);
//
//        var chatLinks = chatLinkRepository.findAll();
//
//        assertThat(chatLinks).hasSize(1);
//        assertThat(chatLinks.getFirst()).isEqualTo(savedChatLink);
//    }
}
