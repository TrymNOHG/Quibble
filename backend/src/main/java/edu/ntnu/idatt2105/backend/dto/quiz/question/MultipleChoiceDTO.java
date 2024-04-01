package edu.ntnu.idatt2105.backend.dto.quiz.question;

import lombok.NonNull;

public record MultipleChoiceDTO(
        @NonNull Long multipleChoiceId,
        @NonNull String alternative,
        boolean isCorrect,
        @NonNull Long questionId) {
}
