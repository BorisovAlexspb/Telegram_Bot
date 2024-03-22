package edu.java.domain;

import edu.java.model.Link;
import java.util.List;

public interface ChatLinkRepository {

    Integer remove(Long chatId, Link link);

    Integer add(Integer linkId, Long chatId);

    List<Link> findLinksByChat(long chatId);

    boolean isLinkPresent(Link link);

    boolean isLinkPresentInChat(Link link, long chatID);

    List<Long> findAllChatIdsByLinkId(Long linkId);
}
