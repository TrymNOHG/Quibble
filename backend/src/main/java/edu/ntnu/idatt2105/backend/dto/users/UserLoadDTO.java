package edu.ntnu.idatt2105.backend.dto.users;

import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import edu.ntnu.idatt2105.backend.model.quiz.QuizFeedback;
import edu.ntnu.idatt2105.backend.model.quiz.QuizHistory;
import lombok.Builder;
import lombok.NonNull;

import java.util.Set;

/**
 * This Data Transfer Object contains the information for laoding a user.
 *
 * @param userId        The user's Id.
 * @param username      The username of the user.
 * @param email         The email of the user.
 * @param quizHistory   The user's quiz history.
 * @param feedbacks     The feedbacks sent by the user.
 * @param quizzes       The quizzes created by the user.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Builder
public record UserLoadDTO(@NonNull Long userId, @NonNull String username, @NonNull String email,
                          @NonNull Set<QuizHistory> quizHistory, @NonNull Set<QuizFeedback> feedbacks,
                          @NonNull Set<QuizAuthor> quizzes) {
}