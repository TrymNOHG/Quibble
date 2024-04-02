package edu.ntnu.idatt2105.backend.dto.quiz.question;

import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * This record provides a data transfer object for editing a question.
 * @param quizId        The id of the quiz.
 * @param question      Potentially edited question.
 * @param answer        Potentially edited answer.
 * @param type          Potentially edited type.
 * @param choices       Potentially edited choices.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 02.04.2024
 */
@Builder
public record QuestionEditDTO(@NonNull Long quizId,
                              @NonNull Long questionId,
                              @Nullable String question,
                              @Nullable String answer,
                              @Nullable QuestionType type,
                              @Nullable Set<MultipleChoiceDTO> choices) {
}
