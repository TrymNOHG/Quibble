package edu.ntnu.idatt2105.backend.dto.quiz.question;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * This record provides the data transfer object for the multiple choice model.
 *
 * @param multipleChoiceId  The id of the multiple choice object.
 * @param alternative       The option.
 * @param isCorrect         Whether the choice is correct.
 * @param questionId        The id of the respective question.
 */
@Builder
public record MultipleChoiceDTO(
        @Nullable Long multipleChoiceId,
        @NonNull String alternative,
        boolean isCorrect,
        @NonNull Long questionId) {
}
