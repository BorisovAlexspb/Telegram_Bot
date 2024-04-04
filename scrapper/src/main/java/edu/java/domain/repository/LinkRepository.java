package edu.java.domain.repository;

import edu.java.dto.entity.jdbc.Link;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {

    void addLink(Link link);

    void deleteLink(String url);

    Link findLink(String url);

    List<Link> findAll();

    void updateLastUpdateAndCheckTime(String url, OffsetDateTime lastUpdatedAt, OffsetDateTime checkedAt);

    List<Link> findOutdatedLinks(Duration threshold);
}
