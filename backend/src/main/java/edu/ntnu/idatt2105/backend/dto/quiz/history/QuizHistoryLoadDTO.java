package edu.ntnu.idatt2105.backend.dto.quiz.history;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * This record provides a data transfer object for loading a historical quiz event.
 *
 * @param quizId        The id of the quiz.
 * @param userId        The id of the user.
 * @param timestamp     The timestamp of the event.
 * @param score         The score.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Builder
public record QuizHistoryLoadDTO(@NonNull Long quizId, @NonNull Long userId, @NonNull LocalDateTime timestamp,
                                 double score) {
}
