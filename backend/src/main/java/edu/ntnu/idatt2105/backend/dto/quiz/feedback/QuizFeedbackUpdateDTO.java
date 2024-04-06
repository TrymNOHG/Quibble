package edu.ntnu.idatt2105.backend.dto.quiz.feedback;

public record QuizFeedbackUpdateDTO(
        Long feedbackId,
        int stars,
        String feedback
) {
}
