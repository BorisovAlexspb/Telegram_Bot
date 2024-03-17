package edu.java.domain;

import edu.java.model.ChatLink;
import edu.java.model.Link;
import java.util.List;
import java.util.Optional;

public interface ChatLinkRepository {

    Optional<ChatLink> find(Integer linkId, Long chatId);

    Integer remove(Long chatId, Link link);

    Integer add(Integer linkId, Long chatId);

    List<ChatLink> findAll();

    List<Link> findLinksByChat(long chatId);

    boolean isLinkPresent(Link link);

    boolean isLinkPresentInChat(Link link, long chatID);

    List<Long> findAllChatIdsByLinkId(Long linkId);
}
