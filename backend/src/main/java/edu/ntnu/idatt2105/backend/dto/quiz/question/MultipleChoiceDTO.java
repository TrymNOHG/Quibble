package edu.ntnu.idatt2105.backend.dto.quiz.question;

import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.NonNull;
import lombok.ToString;

public record MultipleChoiceDTO(@NonNull Long multipleChoiceId, @NonNull String alternative, boolean isCorrect,
                                @NonNull Long questionId) {
}
