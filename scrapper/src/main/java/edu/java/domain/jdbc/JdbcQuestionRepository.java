package edu.java.domain.jdbc;

import edu.java.domain.QuestionRepository;
import edu.java.dto.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcQuestionRepository implements QuestionRepository {

    private final JdbcClient jdbcClient;

    @Override
    public Question saveQuestion(Question question) {
        jdbcClient.sql("INSERT INTO question (Answer_count, Link_id) VALUES (?, ?))")
            .params(question.getAnswerCount(), question.getLinkId());
        return findByLinkId(question.getLinkId());
    }

    @Override
    public Question findByLinkId(Long linkId) {
        return jdbcClient.sql("SELECT ID, Answer_count, Link_id FROM question WHERE Link_id = ?")
            .param(linkId)
            .query(Question.class)
            .single();
    }
}
