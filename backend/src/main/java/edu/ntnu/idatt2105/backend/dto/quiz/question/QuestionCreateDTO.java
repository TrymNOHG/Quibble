package edu.ntnu.idatt2105.backend.dto.quiz.question;

import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * This record
 * @param quizId
 * @param question
 * @param answer
 * @param type
 * @param choices
 */
@Builder
public record QuestionCreateDTO(@NonNull Long quizId,
                                @Nullable String question,
                                @Nullable String answer,
                                @Nullable QuestionType type,
                                @Nullable Set<MultipleChoiceCreateDTO> choices) {
}
