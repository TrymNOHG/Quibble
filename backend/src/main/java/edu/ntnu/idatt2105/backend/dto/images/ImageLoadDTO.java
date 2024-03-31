package edu.ntnu.idatt2105.backend.dto.images;

import lombok.Builder;
import lombok.NonNull;

/**
 * This record provides a data transfer object for images.
 * @param contentType   The type of image.
 * @param image         The actual image.
 */
@Builder
public record ImageLoadDTO(@NonNull String contentType, byte[] image) {
}
