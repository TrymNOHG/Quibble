package edu.ntnu.idatt2105.backend.dto.websocket;

import lombok.Builder;

/**
 * This record provides the DTO for alternative.
 * @param alternative   The alternative in a multiplayer game.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.0 04.04.2024
 */
@Builder
public record AlternativeDTO(
        String alternative
) {
}
