package edu.ntnu.idatt2105.backend.dto.quiz.question;

import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import lombok.Builder;
import lombok.NonNull;

import java.util.Set;

@Builder
public record QuestionDTO(@NonNull Long questionId,
                          String question,
                          String answer,
                          QuestionType type,
                          Difficulty difficulty,
                          Set<MultipleChoiceDTO> choices,
                          @NonNull Long quizId) {
}
