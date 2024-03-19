package edu.java.domain.jdbc;

import edu.java.domain.LinkRepository;
import edu.java.model.Link;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private final JdbcClient jdbcClient;

    @Override
    public Integer addLink(Link link) {
        return jdbcClient.sql("INSERT INTO link(URL) VALUES (?) returning ID")
            .param(link.getUrl())
            .query(Integer.class)
            .single();
    }

    @Override
    public Integer deleteLink(String url) {
        return jdbcClient.sql("DELETE FROM link WHERE URL = ?")
            .param(url)
            .update();
    }

    @Override
    public Link findLink(String url) {
        return jdbcClient.sql("SELECT  * FROM link WHERE URL = ? ")
            .param(url)
            .query(Link.class)
            .single();
    }

    @Override
    public List<Link> findAll() {
        return jdbcClient.sql("SELECT * FROM link")
            .query(Link.class)
            .list();
    }

    @Override
    public void updateLastUpdateAndCheckTime(String url, OffsetDateTime lastUpdatedAt, OffsetDateTime checkedAt) {
        jdbcClient.sql("UPDATE link SET Last_updated = ?, Checked_at = ? WHERE URL = ?")
            .params(lastUpdatedAt, checkedAt, url);
    }

    public List<Link> findOutdatedLinks(Duration threshold) {
        LocalDateTime thresholdDateTime = LocalDateTime.now().minus(threshold);
        OffsetDateTime thresholdOffsetDateTime = thresholdDateTime.atOffset(ZoneOffset.UTC);

        return jdbcClient.sql("SELECT * FROM link WHERE Checked_at < ?")
            .param(thresholdOffsetDateTime)
            .query(Link.class)
            .list();
    }
}
