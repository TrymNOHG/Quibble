package edu.ntnu.idatt2105.backend.controller.pub.users;

import edu.ntnu.idatt2105.backend.dto.users.MultipleUserDTO;
import edu.ntnu.idatt2105.backend.model.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This interface outlines the various functionality the public endpoint for user should have.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
public interface IUserController {

    // Load basic info, Find user by search, register

    /**
     * This method retrieves user's through fuzzy searching by username.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @GetMapping(
            value="/get"
    )
    @Operation(summary = "This method retrieves user's through fuzzy searching by username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of users.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Unsuccessful retrieval of users.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) })
    }
    )
    ResponseEntity<MultipleUserDTO> findUsersByUsername(@RequestParam @NonNull String username);




}