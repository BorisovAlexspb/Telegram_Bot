package edu.java.domain;

import edu.java.model.Link;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {

    Integer addLink(Link link);

    Integer deleteLink(String url);

    Link findLink(String url);

    List<Link> findAll();

    void updateLastUpdateAndCheckTime(String url, OffsetDateTime lastUpdatedAt, OffsetDateTime checkedAt);
}
