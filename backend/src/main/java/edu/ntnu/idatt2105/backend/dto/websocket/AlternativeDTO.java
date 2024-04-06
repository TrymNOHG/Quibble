package edu.ntnu.idatt2105.backend.dto.websocket;

import lombok.Builder;

@Builder
public record AlternativeDTO(
        String alternative
) {
}
