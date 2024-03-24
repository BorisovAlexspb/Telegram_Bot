package edu.java.service.update;

import edu.java.client.github.GitHubClient;
import edu.java.client.scrapper.BotClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.domain.repository.jdbc.JdbcChatLinkRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcQuestionRepository;
import edu.java.dto.bot.LinkUpdateRequest;
import edu.java.dto.entity.jdbc.LinkType;
import edu.java.dto.entity.jdbc.Question;
import edu.java.dto.entity.jdbc.UpdateInfo;
import edu.java.dto.entity.jdbc.Link;
import java.time.Duration;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.dto.entity.jdbc.LinkType.GITHUB_REPO;
import static edu.java.dto.entity.jdbc.LinkType.STACKOVERFLOW_QUESTION;

@Service
@RequiredArgsConstructor
public class JdbcLinkUpdater implements LinkUpdater {
    private static final Duration THRESHOLD = Duration.ofDays(1L);
    private final JdbcChatLinkRepository chatLinkRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcQuestionRepository questionRepository;
    private final StackOverflowClient stackOverflowClient;
    private final GitHubClient githubClient;
    private final BotClient botClient;

    @Override
    @Transactional
    public int update() {
        int updatedCount = 0;
        var outdatedLinks = linkRepository.findOutdatedLinks(THRESHOLD);

        for (Link link : outdatedLinks) {
            UpdateInfo updateInfo = new UpdateInfo(false, link.getUpdatedAt(), "Обновлений нет");

            if (LinkType.getTypeOfLink(link.getUrl()) == GITHUB_REPO) {
                updateInfo = githubClient.checkForUpdate(link);
            } else if (LinkType.getTypeOfLink(link.getUrl()) == STACKOVERFLOW_QUESTION) {
                Question question = questionRepository.findByLinkId(link.getId().longValue());
                updateInfo = stackOverflowClient.checkForUpdate(link, question.getAnswerCount());
            }

            if (updateInfo.isNewUpdate()) {
                botClient.sendUpdate(new LinkUpdateRequest(
                                link.getId().longValue(),
                                link.getUrl(),
                                updateInfo.message(),
                                chatLinkRepository.findAllChatIdsByLinkId(Long.valueOf(link.getId()))
                        )
                );
                updatedCount++;
            }
            linkRepository.updateLastUpdateAndCheckTime(link.getUrl(), updateInfo.updateTime(), OffsetDateTime.now());
        }
        return updatedCount;
    }

}
