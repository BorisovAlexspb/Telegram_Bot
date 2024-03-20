package edu.java.domain.jdbc;

import edu.java.domain.ChatLinkRepository;
import edu.java.model.ChatLink;
import edu.java.model.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {

    private final JdbcClient jdbcClient;

    @Override
    public Optional<ChatLink> find(Integer linkId, Long chatId) {
        return jdbcClient.sql(
                "SELECT Chat_id, Link_id FROM chat_link WHERE Chat_id = ? AND Link_id = ? ")
            .params(chatId, linkId)
            .query(ChatLink.class)
            .optional();
    }

    @Override
    public Integer remove(Long chatId, Link link) {
        return jdbcClient.sql("DELETE FROM chat_link WHERE Chat_id = ? AND Link_id = ?")
            .params(chatId, link.getId())
            .update();
    }

    @Override
    public Integer add(Integer linkId, Long chatId) {
        return jdbcClient.sql("INSERT INTO chat_link VALUES (?, ?)")
            .params(linkId, chatId)
            .update();
    }

    @Override
    public List<ChatLink> findAll() {
        return jdbcClient.sql("SELECT * FROM chat_link").query(ChatLink.class).list();
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
