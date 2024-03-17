package edu.java.domain;

import edu.java.dto.Question;
import java.util.Optional;

public interface QuestionRepository {

    Question saveQuestion(Question question);

    Optional<Question> findByLinkId(Long linkId);
}
