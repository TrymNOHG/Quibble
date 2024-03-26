package edu.ntnu.idatt2105.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserRegisterDTO(
        @JsonProperty("username") String username,
        @JsonProperty("password") String password,
        @JsonProperty("email") String email,
        @JsonProperty("profile_picture") String profilePicLink
) {}
