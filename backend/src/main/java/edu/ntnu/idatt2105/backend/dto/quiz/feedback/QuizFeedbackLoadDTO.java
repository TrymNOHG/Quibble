package edu.ntnu.idatt2105.backend.dto.quiz.feedback;

import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

/**
 * This record provides a data transfer object for a quiz feedback.
 * @param quizId        The id of the quiz.
 * @param userId        The id of the user.
 * @param feedbackId    The id of the feedback.
 * @param stars         Number of stars.
 * @param feedback      The feedback message.
 */
@Builder
public record QuizFeedbackLoadDTO(@NonNull Long quizId, @NonNull Long userId,@NonNull Long feedbackId,
                                  int stars, @Nullable String feedback) {
}
