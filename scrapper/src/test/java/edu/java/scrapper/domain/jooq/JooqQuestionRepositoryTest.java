package edu.java.scrapper.domain.jooq;

import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.domain.repository.jooq.JooqQuestionRepository;
import edu.java.dto.entity.jdbc.Question;
import edu.java.dto.entity.jdbc.Link;
import edu.java.scrapper.IntegrationTest;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static edu.java.domain.jooq.Tables.QUESTIONS;
import static org.assertj.core.api.Assertions.assertThat;

;

@SpringBootTest
public class JooqQuestionRepositoryTest extends IntegrationTest {

    @Autowired
    private JooqLinkRepository linkRepository;

    @Autowired
    private JooqQuestionRepository questionRepository;

    @Autowired
    private DSLContext dslContext;

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
        return dslContext.select(QUESTIONS.fields())
                .from(QUESTIONS)
                .fetchInto(Question.class);
    }
}

