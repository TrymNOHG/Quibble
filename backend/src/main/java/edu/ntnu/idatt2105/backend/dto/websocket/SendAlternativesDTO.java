package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

/**
 * DTO for sending alternatives. This is used to send alternatives to the frontend.
 *
 * @param questionType The type of question that the alternatives are for.
 * @param options The alternatives for the question.
 * @author Brage halvorsen Kvamme
 * @version 1.0 31.03.2024
 */
@Builder
public record SendAlternativesDTO(
        @JsonProperty String questionType,
        @JsonProperty List<AlternativeDTO> options
) {
}
