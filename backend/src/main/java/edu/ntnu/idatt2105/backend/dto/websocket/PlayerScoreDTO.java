package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * DTO for the player score. This is used to send the player score to the frontend.
 * The player score is the profile picture, username and score of a player.
 *
 * @param imageId The profile picture of the player.
 * @param username The username of the player.
 * @param score The score of the player.
 * @version 1.0 31.03.2024
 * @author Brage Halvorsen Kvamme
 */
@Builder
public record PlayerScoreDTO(
        @JsonProperty String imageId,
        @JsonProperty String username,
        @JsonProperty int score

) {}
