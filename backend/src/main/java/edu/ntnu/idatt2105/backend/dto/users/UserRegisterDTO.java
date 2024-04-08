package edu.ntnu.idatt2105.backend.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * This record provides the data transfer object for registering a user.
 * @param username      The username of the user.
 * @param password      The password of the user.
 * @param email         The email of the user.
 *
 * @author Brage Halvorsen Kvamme, Trym Hamer Gudvangen
 * @version 1.0 04.04.2024
 */
@Builder
public record UserRegisterDTO(
        @JsonProperty("username") String username,
        @JsonProperty("password") String password,
        @JsonProperty("email") String email

) {}
