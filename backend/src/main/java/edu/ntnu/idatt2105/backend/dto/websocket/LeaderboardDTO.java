package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.ntnu.idatt2105.backend.util.Player;
import jakarta.annotation.Nullable;
import lombok.Builder;

import java.util.List;

@Builder
public record LeaderboardDTO(
        @Nullable @JsonProperty List<PlayerScoreDTO> player
) {}

