package edu.ntnu.idatt2105.backend.dto.quiz.question;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Builder
public record MultipleChoiceDTO(
        @Nullable Long multipleChoiceId,
        @NonNull String alternative,
        boolean isCorrect,
        @NonNull Long questionId) {
}
