package edu.java.service.update;

import edu.java.client.github.GitHubClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.domain.repository.jpa.JpaChatLinkRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.dto.bot.LinkUpdateRequest;
import edu.java.dto.entity.jdbc.LinkType;
import edu.java.dto.entity.jdbc.UpdateInfo;
import edu.java.dto.entity.jpa.Link;
import edu.java.dto.entity.jpa.StackOverflowQuestionLink;
import edu.java.dto.stackoverflow.QuestionResponse;
import edu.java.service.notification.NotificationService;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkUpdater implements LinkUpdater {
    private static final Duration THRESHOLD = Duration.ofDays(1L);

    private final JpaChatLinkRepository chatLinkRepository;
    private final JpaLinkRepository linkRepository;
    private final StackOverflowClient stackOverflowClient;
    private final GitHubClient githubClient;
//    private final BotClient botClient;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public int update() {
        int updatedCount = 0;

        List<Link> outdatedLinks = linkRepository.findOutdatedLinks(OffsetDateTime.now().minus(THRESHOLD));

        for (Link link : outdatedLinks) {
            UpdateInfo updateInfo;

            if (LinkType.getTypeOfLink(link.getUrl()) == LinkType.GITHUB_REPO) {
                updateInfo = githubClient.checkForUpdate(link.getUrl(), link.getLastUpdatedAt());
            } else {
                QuestionResponse question = stackOverflowClient
                    .getLastModificationTime(stackOverflowClient.getQuestionId(link.getUrl()))
                    .items().getFirst();

                updateInfo = stackOverflowClient.checkForUpdate(
                    link.getUrl(),
                    link.getLastUpdatedAt(),
                    ((StackOverflowQuestionLink) link).getAnswerCount()
                );
                if (updateInfo.isNewUpdate()) {
                    linkRepository.updateAnswerCountByUrl(link.getUrl(), question.answerCount());
                }
            }

            if (updateInfo.isNewUpdate()) {
                notificationService.sendUpdateNotification(new LinkUpdateRequest(
                        link.getId(),
                        link.getUrl(),
                        updateInfo.message(),
                        chatLinkRepository.findAllChatIdsByLinkId(link.getId())
                    )
                );
                updatedCount++;
            }

            linkRepository.updateLastUpdatedAtAndCheckedAtByUrl(
                link.getUrl(),
                updateInfo.updateTime(),
                OffsetDateTime.now()
            );
        }

        return updatedCount;
    }
}
