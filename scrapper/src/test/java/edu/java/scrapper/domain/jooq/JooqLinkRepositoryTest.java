package edu.java.scrapper.domain.jooq;

import edu.java.domain.jooq.Tables;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.dto.entity.jdbc.Link;
import edu.java.scrapper.IntegrationTest;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JooqLinkRepository linkRepository;

    @Autowired
    private DSLContext dslContext;

    private static final Link FIRST_LINK = new Link(
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
        linkRepository.addLink(FIRST_LINK);
        linkRepository.addLink(SECOND_LINK);

        var links = getLinks();

        assertThat(links).hasSize(2);
        assertThat(links.getFirst().getUrl()).isEqualTo(FIRST_LINK.getUrl());
        assertThat(links.getLast().getUrl()).isEqualTo(SECOND_LINK.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void deleteLink() {
        linkRepository.addLink(FIRST_LINK);
        linkRepository.addLink(SECOND_LINK);

        linkRepository.deleteLink(SECOND_LINK.getUrl());

        var links = getLinks();

        assertThat(links).hasSize(1);
        assertThat(links.getFirst().getUrl()).isEqualTo(FIRST_LINK.getUrl());

        linkRepository.deleteLink(FIRST_LINK.getUrl());

        links = getLinks();

        assertThat(links).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findLinkByUrl() {
        linkRepository.addLink(FIRST_LINK);

        Link link1 = linkRepository.findLink(FIRST_LINK.getUrl());

        assertThat(link1).isNotNull();

        Link link2 = linkRepository.findLink(SECOND_LINK.getUrl());

        assertThat(link2).isNull();
    }

    private List<Link> getLinks() {
        return dslContext.select(Tables.LINK.fields())
                .from(Tables.LINK)
                .fetchInto(Link.class);
    }
}
