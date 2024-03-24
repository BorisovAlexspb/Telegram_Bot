package edu.java.domain.repository.jooq;

import edu.java.domain.repository.LinkRepository;
import edu.java.model.Link;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.LINK;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {

    private final DSLContext dslContext;

    @Override
    public void addLink(Link link) {
        dslContext.insertInto(LINK)
                .set(LINK.URL, link.getUrl())
                .execute();
    }

    @Override
    public void deleteLink(String url) {
        dslContext.deleteFrom(LINK)
                .where(LINK.URL.eq(url))
                .execute();
    }

    @Override
    public Link findLink(String url) {
        return dslContext.select(LINK.fields())
                .from(LINK)
                .where(LINK.URL.eq(url))
                .fetchOneInto(Link.class);
    }

    @Override
    public List<Link> findAll() {
        return null;
    }

    @Override
    public void updateLastUpdateAndCheckTime(String url, OffsetDateTime lastUpdatedAt, OffsetDateTime checkedAt) {
        dslContext.update(LINK)
                .set(LINK.LAST_UPDATED, lastUpdatedAt)
                .set(LINK.CHECKED_AT, checkedAt)
                .where(LINK.URL.eq(url))
                .execute();
    }

    @Override
    public List<Link> findOutdatedLinks(Duration threshold) {
        LocalDateTime thresholdDateTime = LocalDateTime.now().minus(threshold);
        OffsetDateTime thresholdOffsetDateTime = thresholdDateTime.atOffset(ZoneOffset.UTC);

        return dslContext.select(LINK.fields())
                .from(LINK)
                .where(LINK.CHECKED_AT.lessThan(thresholdOffsetDateTime))
                .fetchInto(Link.class);
    }
}
