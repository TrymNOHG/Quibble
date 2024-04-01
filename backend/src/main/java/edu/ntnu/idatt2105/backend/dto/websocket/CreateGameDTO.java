package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * DTO for creating a game. This is used when a user wants to create a game.
 *
 * @param jwt The jwt of the user that wants to create a game.
 * @param quizId The id of the quiz that the game should be based on.
 *
 * @version 1.0 31.03.2024
 * @author Brage Halvorsen Kvamme
 */
@Builder
public record CreateGameDTO(
        @NotNull @JsonProperty String jwt,
        @NotNull @JsonProperty long quizId
) { }
