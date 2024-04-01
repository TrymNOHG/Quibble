package edu.ntnu.idatt2105.backend.dto.quiz.category;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * This record provides a data transfer object for a quiz category.
 * @param quizCategoryId
 * @param category
 * @param categoryDescription
 *
 * @author Trym Hamer Gudvangen
 * @version 01.04.2024
 */
@Builder
public record QuizCategoryLoadDTO(
        @NonNull Long quizCategoryId,
        @NonNull String category,
        @Nullable String categoryDescription,
        @NonNull Long quizId
) {}
