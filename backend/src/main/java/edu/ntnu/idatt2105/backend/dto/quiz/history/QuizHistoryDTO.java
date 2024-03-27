package edu.ntnu.idatt2105.backend.dto.quiz.history;

import lombok.Builder;
import lombok.NonNull;

/**
 * This record provides a data transfer object for a historical quiz event.
 *
 * @param quizId    The id of the quiz.
 * @param userId    The id of the user.
 * @param score     The user's score.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Builder
public record QuizHistoryDTO(@NonNull Long quizId, @NonNull Long userId, double score) {
}
