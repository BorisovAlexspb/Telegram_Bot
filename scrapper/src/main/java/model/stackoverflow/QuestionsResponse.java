package model.stackoverflow;

import java.util.List;

public record QuestionsResponse(List<QuestionResponse> items) {
}
