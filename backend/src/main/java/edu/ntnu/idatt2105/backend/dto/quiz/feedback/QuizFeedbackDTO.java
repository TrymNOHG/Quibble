package edu.ntnu.idatt2105.backend.dto.quiz.feedback;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * This record provides a data transfer object for feedback on a quiz.
 *
 * @param quizId    The id of the quiz.
 * @param userId    The id of the user.
 * @param stars     The number of stars awarded by the user.
 * @param comment   An optional comment left on the quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Builder
public record QuizFeedbackDTO(@NonNull Long quizId, @NonNull Long userId, int stars, @Nullable String comment) {
}
