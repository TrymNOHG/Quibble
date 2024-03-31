package edu.ntnu.idatt2105.backend.dto.quiz;

import lombok.Builder;
import lombok.NonNull;

import java.util.Set;

/**
 * This record provides a data transfer object for multiple quiz DTOs.
 * @param quizzes   The quiz DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 31.03.2024
 */
@Builder
public record QuizLoadAllDTO (@NonNull Set<QuizLoadDTO> quizzes) {
}
