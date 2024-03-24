package edu.java.domain.repository.jooq;

import edu.java.domain.repository.ChatRepository;
import edu.java.model.Chat;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.CHAT;


@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {

    private final DSLContext dslContext;

    @Override
    public void add(Long chatId) {
        dslContext.insertInto(CHAT)
                .set(CHAT.ID, chatId)
                .execute();
    }

    @Override
    public void remove(Long chatId) {
        dslContext.deleteFrom(CHAT)
                .where(CHAT.ID.eq(chatId))
                .execute();
    }

    @Override
    public Chat find(Long chatId) {
        return dslContext.select(CHAT.fields())
                .from(CHAT)
                .where(CHAT.ID.eq(chatId))
                .fetchOneInto(Chat.class);
    }
}
