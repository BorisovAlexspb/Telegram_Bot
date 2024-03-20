package edu.java.service.jdbc;

import edu.java.domain.jdbc.JdbcChatLinkRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.dto.bot.AddLinkRequest;
import edu.java.dto.bot.LinkResponse;
import edu.java.dto.bot.ListLinksResponse;
import edu.java.dto.bot.RemoveLinkRequest;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.model.Link;
import edu.java.service.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final JdbcLinkRepository linkRepository;

    private final JdbcChatLinkRepository chatLinkRepository;

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

        linkRepository.addLink(linkToSave);

        chatLinkRepository.add(linkToSave.getId(), chatId);
        return new LinkResponse(linkToSave.getId().longValue(), linkToSave.getUrl());
    }

    @Override
    @Transactional
    public LinkResponse remove(long chatId, RemoveLinkRequest removeLinkRequest) {
        Link link = linkRepository.findLink(removeLinkRequest.uri().toString());
        if (link == null || !chatLinkRepository.isLinkPresentInChat(link, chatId)) {
            throw new LinkAlreadyTrackedException("Can not remove cause i did not track this link");
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
