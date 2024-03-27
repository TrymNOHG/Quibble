package edu.ntnu.idatt2105.backend.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserRegisterDTO(
        @NotNull(message = "Username must be between 4 and 64 characters long.") @JsonProperty("username") String username,
        @NotNull(message = "Password must be between 8 and 64 characters long.")@JsonProperty("password") String password,
        @NotNull(message = "Must be a valid email format") @JsonProperty("email") String email,
        @Nullable @JsonProperty("profile_picture") String profilePicLink
) {}
