package edu.ntnu.idatt2105.backend.dto.quiz.question;

import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

public record QuestionCreateDTO(@NonNull Long quizId,
                                @NonNull String question,
                                @NonNull String answer,
                                @NonNull QuestionType type,
                                @Nullable Set<MultipleChoiceDTO> choices) {
}
