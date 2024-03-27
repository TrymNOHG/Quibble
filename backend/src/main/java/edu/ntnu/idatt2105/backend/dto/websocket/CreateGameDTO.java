package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateGameDTO(
        @JsonProperty String jwt,
        @JsonProperty long userId,
        @JsonProperty long quizId
) {
}
