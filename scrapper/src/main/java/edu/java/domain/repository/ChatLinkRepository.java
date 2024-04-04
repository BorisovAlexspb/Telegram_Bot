package edu.java.domain.repository;

import edu.java.dto.entity.jdbc.Link;
import java.util.List;

public interface ChatLinkRepository {

    void remove(Long chatId, Link link);

    void add(Integer linkId, Long chatId);


    List<Link> findLinksByChat(long chatId);

    boolean isLinkPresent(Link link);

    boolean isLinkPresentInChat(Link link, long chatID);

    List<Long> findAllChatIdsByLinkId(Long linkId);
}
