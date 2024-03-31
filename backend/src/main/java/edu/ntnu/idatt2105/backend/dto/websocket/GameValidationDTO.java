package edu.ntnu.idatt2105.backend.dto.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record GameValidationDTO(
        @NotNull @JsonProperty String jwt
) {}
