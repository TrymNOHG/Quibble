package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for joining a game. This is used when a user wants to join a game. The user can be anonymous.
 *
 * @param jwt The jwt of the user that wants to join a game.
 */
public record JoinGameDTO (
    @Nullable @JsonProperty String jwt,
    @Nullable @JsonProperty String username,
    @NotNull @JsonProperty String code
) {}
