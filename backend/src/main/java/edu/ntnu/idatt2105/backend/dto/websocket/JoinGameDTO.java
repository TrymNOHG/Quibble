package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * DTO for joining a game. This is used when a user wants to join a game. User is anonymous if the jwt is not provided.
 * Username must then be provided.
 *
 * @param jwt The jwt of the user that wants to join a game.
 * @param code The code of the game that the user wants to join.
 * @param username The username of the user that wants to join a game.
 * @author Brage Halvorsen Kvamme
 * @version 1.0 31.03.2024
 */
@Builder
public record JoinGameDTO (
    @Nullable @JsonProperty String jwt,
    @Nullable @JsonProperty String username,
    @NotNull @JsonProperty String code,
    @Nullable @JsonProperty String imageId
) {}
