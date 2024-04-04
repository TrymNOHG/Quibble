package edu.ntnu.idatt2105.backend.dto.quiz.question;

import lombok.Builder;
import lombok.NonNull;

/**
 * This record provides a data transfer object for creating multiple choice DTO.
 * @param alternative   The alternative.
 * @param isCorrect     Whether the choice is correct or not.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 02.04.2024
 */
@Builder
public record MultipleChoiceCreateDTO(@NonNull String alternative, boolean isCorrect) {
}
