package edu.ntnu.idatt2105.backend.dto.quiz.question;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record MultipleChoiceDTO(
        @NonNull Long multipleChoiceId,
        @NonNull String alternative,
        boolean isCorrect,
        @NonNull Long questionId) {
}
