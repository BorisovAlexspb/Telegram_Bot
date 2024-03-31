package edu.java.service.jpa;

import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.domain.repository.jpa.JpaChatLinkRepository;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.dto.bot.AddLinkRequest;
import edu.java.dto.bot.LinkResponse;
import edu.java.dto.bot.ListLinksResponse;
import edu.java.dto.bot.RemoveLinkRequest;
import edu.java.dto.entity.jdbc.LinkType;
import edu.java.dto.entity.jpa.Chat;
import edu.java.dto.entity.jpa.ChatLink;
import edu.java.dto.entity.jpa.GithubRepositoryLink;
import edu.java.dto.entity.jpa.Link;
import edu.java.dto.entity.jpa.StackOverflowQuestionLink;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.service.LinkService;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {

    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;
    private final JpaChatLinkRepository chatLinkRepository;
    private final StackOverflowClient stackOverflowClient;

    @Override
    public LinkResponse add(long chatId, AddLinkRequest addLinkRequest) {
        Optional<Link> link = linkRepository.findByUrl(addLinkRequest.link());

        link.ifPresent(l -> {
            if (chatLinkRepository.existsByChatIdAndLinkId(chatId, l.getId())) {
                throw new LinkAlreadyTrackedException("link is already tracked");
            }
        });

        Chat chat = chatRepository.getReferenceById(chatId);
        Link savedLink = link.orElseGet(() -> {
            if (LinkType.getTypeOfLink(addLinkRequest.link()) == LinkType.GITHUB_REPO) {
                return linkRepository.save(GithubRepositoryLink.builder()
                    .url(addLinkRequest.link())
                    .checkedAt(OffsetDateTime.now())
                    .lastUpdatedAt(OffsetDateTime.now())
                    .chatLinks(new ArrayList<>())
                    .build()
                );
            } else {
                var question = stackOverflowClient
                    .getLastModificationTime(stackOverflowClient.getQuestionId(addLinkRequest.link()))
                    .items()
                    .getFirst();
                return linkRepository.save(StackOverflowQuestionLink.builder()
                    .url(addLinkRequest.link())
                    .checkedAt(OffsetDateTime.now())
                    .lastUpdatedAt(OffsetDateTime.now())
                    .chatLinks(new ArrayList<>())
                    .answerCount(question.answerCount())
                    .build()
                );
            }
        });

        ChatLink chatLink = new ChatLink();
        chatLink.setChat(chat);
        chatLink.setLink(savedLink);

        chatLinkRepository.save(chatLink);
        return new LinkResponse(savedLink.getId(), savedLink.getUrl());
    }

    @Override
    @Transactional
    public LinkResponse remove(long chatId, RemoveLinkRequest removeLinkRequest) {
        Optional<ChatLink> chatLinkOptional =
            chatLinkRepository.findChatLinkByChatIdAndLinkUrl(chatId, removeLinkRequest.link());

        if (chatLinkOptional.isEmpty()) {
            throw new LinkNotFoundException("Can not remove cause I did not track this link");
        }

        ChatLink chatLink = chatLinkOptional.get();

        chatLinkRepository.delete(chatLink);

        var link = chatLink.getLink();

        if (!chatLinkRepository.existsByLinkId(link.getId())) {
            linkRepository.delete(link);
        }

        return new LinkResponse(link.getId(), link.getUrl());
    }

    @Override
    public ListLinksResponse listAll(long chatId) {
        List<LinkResponse> links = chatLinkRepository.findAllLinksByChatId(chatId)
            .stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList();
        return new ListLinksResponse(links);
    }
}
