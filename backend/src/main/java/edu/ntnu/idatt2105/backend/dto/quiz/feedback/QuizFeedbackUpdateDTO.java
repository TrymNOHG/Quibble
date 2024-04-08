package edu.ntnu.idatt2105.backend.dto.quiz.feedback;

/**
 * This record provides a data transfer object for updating quiz feedback.
 *
 * @param feedbackId    The id of the feedback.
 * @param stars         The number of stars.
 * @param feedback      The feedback.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.1 06.04.2024
 */
public record QuizFeedbackUpdateDTO(
        Long feedbackId,
        int stars,
        String feedback
) {
}
