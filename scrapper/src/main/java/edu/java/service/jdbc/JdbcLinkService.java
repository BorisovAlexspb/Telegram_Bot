package edu.java.service.jdbc;

import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.domain.repository.jdbc.JdbcChatLinkRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcQuestionRepository;
import edu.java.dto.bot.AddLinkRequest;
import edu.java.dto.bot.LinkResponse;
import edu.java.dto.bot.ListLinksResponse;
import edu.java.dto.bot.RemoveLinkRequest;
import edu.java.dto.entity.jdbc.Link;
import edu.java.dto.entity.jdbc.LinkType;
import edu.java.dto.entity.jdbc.Question;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.service.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.dto.entity.jdbc.LinkType.STACKOVERFLOW_QUESTION;

@SuppressWarnings("LineLength")
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final JdbcLinkRepository linkRepository;
    private final JdbcChatLinkRepository chatLinkRepository;
    private final JdbcQuestionRepository questionRepository;
    private final StackOverflowClient stackOverflowWebClient;

    @Override
    @Transactional
    public LinkResponse add(long chatId, AddLinkRequest addLinkRequest) {
        Link link = linkRepository.findLink(addLinkRequest.link());
        if (link != null && chatLinkRepository.isLinkPresentInChat(link, chatId)) {
            throw new LinkAlreadyTrackedException("link is already tracked");
        }

        Link linkToSave = link;

        if (link == null) {
            linkToSave = new Link(
                null,
                addLinkRequest.link(),
                null,
                null,
                OffsetDateTime.now()
            );
            linkRepository.addLink(linkToSave);
            if (LinkType.getTypeOfLink(linkToSave.getUrl()) == STACKOVERFLOW_QUESTION) {
                var question = stackOverflowWebClient
                    .getLastModificationTime(stackOverflowWebClient.getQuestionId(linkToSave.getUrl()))
                    .items()
                    .getFirst();
                questionRepository.saveQuestion(new Question(
                    null,
                    question.answerCount(),
                    linkToSave.getId().longValue()
                ));
            }

            linkToSave = linkRepository.findLink(linkToSave.getUrl());
        }

        chatLinkRepository.add(linkToSave.getId(), chatId);
        return new LinkResponse(linkToSave.getId().longValue(), linkToSave.getUrl());
    }

    @Override
    @Transactional
    public LinkResponse remove(long chatId, RemoveLinkRequest removeLinkRequest) {
        Link link = linkRepository.findLink(removeLinkRequest.link());
        if (link == null || !chatLinkRepository.isLinkPresentInChat(link, chatId)) {
            throw new LinkNotFoundException("Can not remove cause I did not track this link");
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
