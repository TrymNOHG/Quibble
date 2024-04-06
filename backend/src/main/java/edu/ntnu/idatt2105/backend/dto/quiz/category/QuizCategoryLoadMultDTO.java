package edu.ntnu.idatt2105.backend.dto.quiz.category;

import lombok.Builder;
import lombok.NonNull;

import java.util.Set;

/**
 * This record provides a data transfer object for loading multiple quiz categories.
 * @param quizCategories   The quiz categories as DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 05.04.2024
 */
@Builder
public record QuizCategoryLoadMultDTO(@NonNull Set<QuizCategoryLoadDTO> quizCategories) {
}
