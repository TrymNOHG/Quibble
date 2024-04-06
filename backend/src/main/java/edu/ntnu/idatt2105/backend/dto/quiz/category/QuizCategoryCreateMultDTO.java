package edu.ntnu.idatt2105.backend.dto.quiz.category;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

/**
 * This record provides a data transfer object for creating multiple quiz categories.
 * @param quizId            The id of the quiz.
 * @param categoryIds       The ids of the categories to be added.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 05.04.2024
 */
@Builder
public record QuizCategoryCreateMultDTO(@NonNull Long quizId, @NonNull List<Long> categoryIds) {
}
