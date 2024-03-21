package edu.java.domain.repository.jooq;

import edu.java.domain.repository.ChatLinkRepository;
import edu.java.model.Link;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.CHAT_LINK;
import static edu.java.domain.jooq.Tables.LINK;

@SuppressWarnings("LineLength")
@Repository
@RequiredArgsConstructor
public class JooqChatLinkRepository implements ChatLinkRepository {

    private final DSLContext dslContext;

    @Override
    public void remove(Long chatId, Link link) {
        dslContext.deleteFrom(CHAT_LINK)
                .where(CHAT_LINK.CHAT_ID.eq(chatId).and(CHAT_LINK.LINK_ID.eq(link.getId().longValue())))
                .execute();
    }

    @Override
    public void add(Integer linkId, Long chatId) {
        dslContext.insertInto(CHAT_LINK)
                .set(CHAT_LINK.CHAT_ID, chatId)
                .set(CHAT_LINK.LINK_ID, linkId.longValue())
                .execute();
    }

    @Override
    public List<Link> findLinksByChat(long chatId) {
        return dslContext.select(LINK.fields())
                .from(LINK)
                .join(CHAT_LINK).on(LINK.ID.eq(CHAT_LINK.LINK_ID))
                .where(CHAT_LINK.CHAT_ID.eq(chatId))
                .fetchInto(Link.class);
    }

    @Override
    public boolean isLinkPresent(Link link) {
        return Boolean.TRUE.equals(dslContext.select(
                        DSL.exists(
                                DSL.selectOne()
                                        .from(CHAT_LINK)
                                        .where(CHAT_LINK.LINK_ID.eq(link.getId().longValue()))
                        )
                )
                .fetchOneInto(boolean.class));
    }

    @Override
    public boolean isLinkPresentInChat(Link link, long chatID) {
        return Boolean.TRUE.equals(dslContext.select(
                        DSL.exists(
                                DSL.selectOne()
                                        .from(CHAT_LINK)
                                        .where(CHAT_LINK.CHAT_ID.eq(chatID).and(CHAT_LINK.LINK_ID.eq(link.getId().longValue())))
                        )
                )
                .fetchOneInto(boolean.class));
    }

    @Override
    public List<Long> findAllChatIdsByLinkId(Long linkId) {
        return dslContext.select(CHAT_LINK.CHAT_ID)
                .from(CHAT_LINK)
                .where(CHAT_LINK.LINK_ID.eq(linkId))
                .fetchInto(Long.class);
    }
}
