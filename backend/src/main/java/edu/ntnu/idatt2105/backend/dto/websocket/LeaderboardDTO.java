package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.ntnu.idatt2105.backend.util.Player;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.util.List;

/**
 * DTO for the leaderboard. This is used to send the leaderboard to the frontend.
 * The leaderboard is a list of players and their scores.
 *
 * @param player The list of players and their scores.
 *
 * @version 1.0 31.03.2024
 * @author Brage Halvorsen Kvamme
 */
@Builder
public record LeaderboardDTO(
        @Nullable @JsonProperty List<PlayerScoreDTO> player
) {}

