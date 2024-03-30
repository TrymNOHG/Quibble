package edu.ntnu.idatt2105.backend.dto.quiz.question;

import edu.ntnu.idatt2105.backend.model.quiz.question.Difficulty;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import edu.ntnu.idatt2105.backend.model.quiz.question.Type;
import lombok.NonNull;

import java.util.Set;

public record QuestionDTO(@NonNull Long questionId, String question, String answer,
                          Type type, Difficulty difficulty, Set<MultipleChoiceDTO> choices, @NonNull Long quizId) {
}
