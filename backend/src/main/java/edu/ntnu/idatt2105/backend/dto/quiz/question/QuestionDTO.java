package edu.ntnu.idatt2105.backend.dto.quiz.question;

import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * This record provides the data transfer object for the question model.
 * @param questionId    The id of the question.
 * @param question      The question.
 * @param answer        The answer.
 * @param type          The type of question.
 * @param choices       The choices.
 * @param quizId        The id of the quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 04.04.2024
 */
@Builder
public record QuestionDTO(@NonNull Long questionId,
                          @NonNull String question,
                          @NonNull String answer,
                          @NonNull QuestionType type,
                          @Nullable Set<MultipleChoiceDTO> choices,
                          @NonNull Long quizId) {
}
