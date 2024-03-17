package edu.java.service.update;

import edu.java.client.github.GitHubClient;
import edu.java.client.scrapper.BotClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.domain.jdbc.JdbcChatLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.jdbc.JdbcQuestionRepository;
import edu.java.dto.Question;
import edu.java.dto.UpdateInfo;
import edu.java.dto.bot.LinkUpdateRequest;
import edu.java.model.Link;
import java.time.Duration;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//            OffsetDateTime lastUpdatedAt = link.getUpdatedAt();
            UpdateInfo updateInfo = new UpdateInfo(false, link.getUpdatedAt(), "Обновлений нет");
            if (checkTypeOfLink(link.getUrl()).equals(GIT_HUB_LINK)) {
//                lastUpdatedAt = githubClient.checkForUpdate(link);
                updateInfo = githubClient.checkForUpdate(link);
            } else if (checkTypeOfLink(link.getUrl()).equals(STACK_OVER_FLOW_LINK)) {
//                lastUpdatedAt = stackOverflowClient.checkForUpdate(link);
                Question question = questionRepository.findByLinkId(link.getId().longValue()).get();
                updateInfo = stackOverflowClient.checkForUpdate(link, question.getAnswerCount());
            }

//            if (lastUpdatedAt.isAfter(link.getUpdatedAt()))
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
//            linkRepository.updateLastUpdateAndCheckTime(link.getUrl(), lastUpdatedAt, OffsetDateTime.now());
            linkRepository.updateLastUpdateAndCheckTime(link.getUrl(), updateInfo.updateTime(), OffsetDateTime.now());
        }
        return updatedCount;
    }

    @Override
    public String checkTypeOfLink(String url) {
        if (url.startsWith(GIT_HUB_LINK)) {
            return GIT_HUB_LINK;
        }
        if (url.startsWith(STACK_OVER_FLOW_LINK)) {
            return STACK_OVER_FLOW_LINK;
        }
        return "unknown";
    }
}
