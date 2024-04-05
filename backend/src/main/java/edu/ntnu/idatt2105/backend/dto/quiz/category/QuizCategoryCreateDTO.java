package edu.ntnu.idatt2105.backend.dto.quiz.category;

import lombok.Builder;
import lombok.NonNull;

/**
 * This record provides a data transfer object for creating a new quiz category.
 *
 * @param categoryId        The id of the category.
 * @param quizId            The id of the quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 05.04.2024
 */
@Builder
public record QuizCategoryCreateDTO(
        @NonNull Long categoryId,
        @NonNull Long quizId
) {}
