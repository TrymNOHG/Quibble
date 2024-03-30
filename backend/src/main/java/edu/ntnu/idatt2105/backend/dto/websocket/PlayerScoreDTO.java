package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PlayerScoreDTO(
        @JsonProperty String profilePicture,
        @JsonProperty String username,
        @JsonProperty int score

) {}
