package edu.java.domain.repository.jdbc;

import edu.java.domain.repository.LinkRepository;
import edu.java.dto.entity.jdbc.Link;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private final JdbcClient jdbcClient;

    @Override
    public void addLink(Link link) {
        jdbcClient.sql("INSERT INTO link (url) VALUES (?)")
            .param(link.getUrl())
            .update();
    }

    @Override
    public void deleteLink(String url) {
        jdbcClient.sql("DELETE FROM link WHERE URL = ?")
            .param(url)
            .update();
    }

    @Override
    public Link findLink(String url) {
        try {
            return jdbcClient.sql("""
                                SELECT
                        id,
                        url,
                        last_updated,
                        checked_at,
                        created_at
                    FROM link
                    WHERE url = ?
                    """
                )
                .param(url)
                .query(Link.class)
                .single();
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<Link> findAll() {
        return jdbcClient.sql("""
                SELECT
                    id,
                    url,
                    last_updated,
                    checked_at,
                    created_at
                FROM link
                """
            )
            .query(Link.class)
            .list();
    }

    @Override
    public void updateLastUpdateAndCheckTime(String url, OffsetDateTime lastUpdatedAt, OffsetDateTime checkedAt) {
        jdbcClient.sql("UPDATE link SET Last_updated = ?, Checked_at = ? WHERE url = ?")
            .params(lastUpdatedAt, checkedAt, url);
    }

    public List<Link> findOutdatedLinks(Duration threshold) {
        LocalDateTime thresholdDateTime = LocalDateTime.now().minus(threshold);
        OffsetDateTime thresholdOffsetDateTime = thresholdDateTime.atOffset(ZoneOffset.UTC);

        return jdbcClient.sql("""
                SELECT
                    ID,
                    URL,
                    Last_updated,
                    Checked_at,
                    Created_at
                FROM link
                WHERE Checked_at < ?
                """
            )
            .param(thresholdOffsetDateTime)
            .query(Link.class)
            .list();
    }
}
