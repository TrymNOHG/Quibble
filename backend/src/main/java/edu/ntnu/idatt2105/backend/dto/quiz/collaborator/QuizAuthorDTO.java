package edu.ntnu.idatt2105.backend.dto.quiz.collaborator;

import lombok.Builder;
import lombok.NonNull;

/**
 * This record provides a data transfer object for interactions with quiz collaborator.
 *
 * @param userId    The id of the user.
 * @param quizId    The id of the quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Builder
public record QuizAuthorDTO(@NonNull Long userId, @NonNull Long quizId) {
}
