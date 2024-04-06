package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * DTO for validating a game. This is used to validate that a user is part of game.
 *
 * @param jwt The jwt of the user that wants to validate a game.
 * @version 1.0 31.03.2024
 * @author Brage Halvorsen Kvamme
 */
@Builder
public record GameValidationDTO(
        @NotNull @JsonProperty String jwt
) {}
