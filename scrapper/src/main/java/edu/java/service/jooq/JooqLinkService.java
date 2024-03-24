package edu.java.service.jooq;

import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.domain.repository.jooq.JooqChatLinkRepository;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.domain.repository.jooq.JooqQuestionRepository;
import edu.java.dto.bot.AddLinkRequest;
import edu.java.dto.bot.LinkResponse;
import edu.java.dto.bot.ListLinksResponse;
import edu.java.dto.bot.RemoveLinkRequest;
import edu.java.dto.entity.jdbc.Link;
import edu.java.dto.entity.jdbc.LinkType;
import edu.java.dto.entity.jdbc.Question;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.service.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.dto.entity.jdbc.LinkType.STACKOVERFLOW_QUESTION;

// тот же код что и в Jdbc сервисе
@SuppressWarnings("LineLength")
@Service
@RequiredArgsConstructor
public class JooqLinkService implements LinkService {

    private final JooqLinkRepository linkRepository;
    private final JooqChatLinkRepository chatLinkRepository;
    private final JooqQuestionRepository questionRepository;
    private final StackOverflowClient stackOverflowWebClient;

    @Override
    @Transactional
    public LinkResponse add(long chatId, AddLinkRequest addLinkRequest) {
        Link link = linkRepository.findLink(addLinkRequest.link().toString());
        if (link != null && chatLinkRepository.isLinkPresentInChat(link, chatId)) {
            throw new LinkAlreadyTrackedException("link is already tracked");
        }

        Link linkToSave = new Link(
                null,
                addLinkRequest.link().toString(),
                null,
                null,
                OffsetDateTime.now()
        );

        if (link == null) {
            linkRepository.addLink(linkToSave);
            if (LinkType.getTypeOfLink(linkToSave.getUrl()) == STACKOVERFLOW_QUESTION) {
                var question = stackOverflowWebClient
                        .getLastModificationTime(stackOverflowWebClient.getQuestionId(linkToSave.getUrl()))
                        .items()
                        .getFirst();
                questionRepository.saveQuestion(new Question(null, question.answerCount(), linkToSave.getId().longValue()));
            }
        }

        chatLinkRepository.add(linkToSave.getId(), chatId);
        return new LinkResponse(linkToSave.getId().longValue(), linkToSave.getUrl());
    }

    @Override
    @Transactional
    public LinkResponse remove(long chatId, RemoveLinkRequest removeLinkRequest) {
        Link link = linkRepository.findLink(removeLinkRequest.uri().toString());
        if (link == null || !chatLinkRepository.isLinkPresentInChat(link, chatId)) {
            throw new LinkAlreadyTrackedException("Can not remove cause I did not track this link");
        }

        chatLinkRepository.remove(chatId, link);
        if (!chatLinkRepository.isLinkPresent(link)) {
            linkRepository.deleteLink(link.getUrl());
        }
        return new LinkResponse(link.getId().longValue(), link.getUrl());
    }

    @Override
    @Transactional
    public ListLinksResponse listAll(long chatId) {
        List<LinkResponse> links = chatLinkRepository.findLinksByChat(chatId)
                .stream()
                .map(link -> new LinkResponse(link.getId().longValue(), link.getUrl()))
                .toList();
        return new ListLinksResponse(links, links.size());
    }
}
