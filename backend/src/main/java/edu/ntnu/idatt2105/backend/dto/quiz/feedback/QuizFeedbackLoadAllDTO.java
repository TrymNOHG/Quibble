package edu.ntnu.idatt2105.backend.dto.quiz.feedback;

import edu.ntnu.idatt2105.backend.model.quiz.QuizFeedback;
import lombok.Builder;
import lombok.NonNull;

import java.util.Set;

/**
 * This record provides a data transfer object for quiz feedback.
 * @param feedbacks   The feedbacks as data transfer objects.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 31.03.2024
 */
@Builder
public record QuizFeedbackLoadAllDTO(@NonNull Set<QuizFeedbackLoadDTO> feedbacks) {
}
