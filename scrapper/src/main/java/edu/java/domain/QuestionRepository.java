package edu.java.domain;

import edu.java.dto.Question;

public interface QuestionRepository {

    Question saveQuestion(Question question);

    Question findByLinkId(Long linkId);
}
