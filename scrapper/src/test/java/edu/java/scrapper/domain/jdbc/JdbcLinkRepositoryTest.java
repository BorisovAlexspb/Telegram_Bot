package edu.java.scrapper.domain.jdbc;

import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.dto.entity.jdbc.Link;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Autowired
    private JdbcClient jdbcClient;

    private static final Link LINK = new Link(
        1, "github.com/dummy/dummy_repo",
        null, null, null
    );

    private static final Link SECOND_LINK = new Link(
        2, "github.com/dummy/dummy2_repo",
        null, null, null
    );

    @Test
    @Transactional
    @Rollback
    void saveLink() {
        linkRepository.addLink(LINK);
        linkRepository.addLink(SECOND_LINK);

        var links = getLinks();

        assertThat(links).hasSize(2);
        assertThat(links.getFirst().getUrl()).isEqualTo(LINK.getUrl());
        assertThat(links.getLast().getUrl()).isEqualTo(SECOND_LINK.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void deleteLink() {
        linkRepository.addLink(LINK);
        linkRepository.addLink(SECOND_LINK);

        linkRepository.deleteLink(SECOND_LINK.getUrl());

        var links = getLinks();

        assertThat(links).hasSize(1);
        assertThat(links.getFirst().getUrl()).isEqualTo(LINK.getUrl());

        linkRepository.deleteLink(LINK.getUrl());

        links = getLinks();

        assertThat(links).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findLinkByUrl() {
        linkRepository.addLink(LINK);

        Link link1 = linkRepository.findLink(LINK.getUrl());

        assertThat(link1).isNotNull();

        Link link2 = linkRepository.findLink(SECOND_LINK.getUrl());

        assertThat(link2).isNull();
    }

    private List<Link> getLinks() {
        return jdbcClient.sql("SELECT * FROM link")
            .query(Link.class)
            .list();
    }
}
