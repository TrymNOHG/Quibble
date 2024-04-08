package edu.ntnu.idatt2105.backend.dto.quiz.question;

import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * This record provides the data transfer object for question creation.
 * @param quizId        The id of the quiz.
 * @param question      The question.
 * @param answer        The answer.
 * @param type          The type.
 * @param choices       The choices.
 */
@Builder
public record QuestionCreateDTO(@NonNull Long quizId,
                                @Nullable String question,
                                @Nullable String answer,
                                @Nullable QuestionType type,
                                @Nullable Set<MultipleChoiceCreateDTO> choices) {
}
