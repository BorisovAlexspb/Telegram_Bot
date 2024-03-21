package edu.java.scrapper.domain.jdbc;

import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcQuestionRepository;
import edu.java.dto.entity.Question;
import edu.java.model.Link;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcQuestionRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Autowired
    private JdbcQuestionRepository questionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void saveQuestion() {
        Link link = new Link(
            1, "github.com/dummy/dummy_repo",
            null, null, null
        );

        linkRepository.addLink(link);

        Question question = new Question(null, 2, link.getId().longValue());
        var savedQuestion = questionRepository.saveQuestion(question);

        assertThat(savedQuestion.getAnswerCount()).isEqualTo(question.getAnswerCount());
        assertThat(savedQuestion.getLinkId()).isEqualTo(link.getId());

        var questions = getQuestion();
        assertThat(questions).hasSize(1);
        assertThat(questions).contains(savedQuestion);
    }

    @Test
    @Transactional
    @Rollback
    void findByLinkId() {
        Link link = new Link(
            1, "github.com/dummy/dummy_repo",
            null, null, null
        );

        linkRepository.addLink(link);

        var nullQuestion = questionRepository.findByLinkId(link.getId().longValue());
        assertThat(nullQuestion).isNull();

        Question question = new Question(null, 2, link.getId().longValue());
        var savedQuestion = questionRepository.saveQuestion(question);

        Question actualResult = questionRepository.findByLinkId(link.getId().longValue());

        assertThat(actualResult).isEqualTo(savedQuestion);
    }

    private List<Question> getQuestion() {
        return jdbcTemplate.query(
            "SELECT * FROM question",
            (rs, rowNum) -> new Question(
                rs.getLong("ID"),
                rs.getInt("Answer_count"),
                rs.getLong("Link_id")
            )
        );
    }
}
