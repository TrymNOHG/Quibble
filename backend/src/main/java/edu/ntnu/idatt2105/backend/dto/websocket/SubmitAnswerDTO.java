package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
// has all the same properties as JoinGameDTO, but is used for submitting answers

/**
 * DTO for submitting an answer. This is used when a user wants to submit an answer to a question.
 * Either the jwt or the username must be provided. The game code is not needed as it's possible to find
 * it from the websocket client session id.
 *
 * @param jwt The jwt of the user that wants to submit an answer.
 * @param answer The answer the user wants to submit.
 *
 * @version 1.0 31.03.2024
 * @author Brage Halvorsen Kvamme
 */
@Builder
public record SubmitAnswerDTO(
        @Nullable @JsonProperty String jwt,
        @NotNull @JsonProperty String answer
) {}