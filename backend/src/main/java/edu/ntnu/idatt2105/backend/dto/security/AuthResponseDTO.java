package edu.ntnu.idatt2105.backend.dto.security;

import lombok.NonNull;

/**
 * This record contains the DTO for authentication responses.
 * @param token The token.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
public record AuthResponseDTO(@NonNull String token) {
}
