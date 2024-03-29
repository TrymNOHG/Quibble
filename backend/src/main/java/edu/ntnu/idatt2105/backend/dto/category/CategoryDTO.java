package edu.ntnu.idatt2105.backend.dto.category;

import lombok.Builder;
import lombok.NonNull;

/**
 * This record represents a DTO for a single category's information.
 * @param categoryId            The id of the category in the database.
 * @param categoryName          The display name of the category.
 * @param categoryDescription   A description of the category.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Builder
public record CategoryDTO(@NonNull Long categoryId, @NonNull String categoryName, String categoryDescription) {
}
