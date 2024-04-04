package edu.ntnu.idatt2105.backend.dto.quiz.question;

import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

@Builder
public record QuestionDTO(@NonNull Long questionId,
                          @NonNull String question,
                          @NonNull String answer,
                          @NonNull QuestionType type,
                          @Nullable Set<MultipleChoiceDTO> choices,
                          @NonNull Long quizId) {
}
