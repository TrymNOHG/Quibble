package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SendAlternativesDTO(
        @JsonProperty String questionType,
        @JsonProperty String[] alternatives
) {
}
