package edu.ntnu.idatt2105.backend.dto.category;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

/**
 * This records provides a DTO for sending multiple categories.
 * @param categories    The multiple categories.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Builder
public record MultipleCategoryDTO(@NonNull List<CategoryDTO> categories) {
}
