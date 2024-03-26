package edu.ntnu.idatt2105.backend.controller.pub.users;

import edu.ntnu.idatt2105.backend.dto.security.AuthenticationResponseDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserLoginDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserRegisterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This interface outlines the various functionality the public endpoint for user should have.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
public interface IUserController {

    // Load basic info, Find user by search, register

    /**
     * This endpoint is used to register a new user.
     *
     * @param user The user information.
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PostMapping("/register")
    @Operation(summary = "This endpoint registers a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was successfully registered and sent an " +
                    "authentication token.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "If user already exists.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthenticationResponseDTO.class)
            ))
    })
    ResponseEntity<Object> register(@ParameterObject @RequestBody UserRegisterDTO user, HttpServletResponse response);

    /**
     * This endpoint is used to log in a user.
     *
     * @param user The user information.
     * @return     ResponseEntity showing whether the operation was successful.
     */
    @PostMapping("/login")
    @Operation(summary = "This endpoint logs in a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was successfully logged. Token returned.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "User credentials invalid.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthenticationResponseDTO.class)
            ))
    })
    ResponseEntity<Object> login(@ParameterObject @RequestBody UserLoginDTO user);
}