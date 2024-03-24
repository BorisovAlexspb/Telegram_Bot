package edu.java.domain.repository.jdbc;

import edu.java.domain.repository.ChatLinkRepository;
import edu.java.dto.entity.jdbc.ChatLink;
import edu.java.dto.entity.jdbc.Link;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {

    private final JdbcClient jdbcClient;

    @Override
    public void remove(Long chatId, Link link) {
        jdbcClient.sql("DELETE FROM chat_link WHERE Chat_id = ? AND Link_id = ?")
                .params(chatId, link.getId())
                .update();
    }

    @Override
    public void add(Integer linkId, Long chatId) {
        jdbcClient.sql("INSERT INTO chat_link VALUES (?, ?)")
                .params(linkId, chatId)
                .update();
    }

    @Override
    public List<Link> findLinksByChat(long chatId) {
        return jdbcClient.sql("""
                        SELECT
                           l.ID,
                           l.URL,
                           l.Checked_at,
                           l.Last_updated,
                           l.Created_at
                        FROM link l
                        JOIN
                            chat_link cl ON l.Id = cl.Link_id
                        WHERE cl.Chat_id = ?
                        """)
                .param(chatId)
                .query(Link.class)
                .list();
    }

    @Override
    public boolean isLinkPresent(Link link) {
        return jdbcClient.sql("SELECT * FROM chat_link WHERE Link_id = ?")
                .param(link.getId())
                .query(ChatLink.class)
                .optional()
                .isPresent();
    }

    @Override
    public boolean isLinkPresentInChat(Link link, long chatID) {
        return jdbcClient.sql("SELECT 1 FROM chat_link WHERE Link_id = ? AND Chat_id = ?")
                .params(link.getId(), chatID)
                .query(ChatLink.class)
                .optional()
                .isPresent();
    }

    @Override
    public List<Long> findAllChatIdsByLinkId(Long linkId) {
        return jdbcClient.sql("""
                        SELECT chat_id FROM .chat_link
                        WHERE Link_id = ?
                        """
                )
                .param(linkId)
                .query(Long.class)
                .list();
    }
}
