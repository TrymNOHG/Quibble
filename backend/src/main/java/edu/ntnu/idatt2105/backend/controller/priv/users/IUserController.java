package edu.ntnu.idatt2105.backend.controller.priv.users;

import edu.ntnu.idatt2105.backend.dto.users.UserUpdateDTO;
import edu.ntnu.idatt2105.backend.model.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This interface outlines the various functionality the private endpoint for user's should have.
 * An authorized user should be able to update and delete their profile.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
public interface IUserController {

    /**
     * This method updates the user's information.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PatchMapping(
            value="/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method updates the user's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful update of user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized update of user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) })
    }
    )
    ResponseEntity<String> updateUser(@RequestBody UserUpdateDTO userUpdateDTO, Authentication authentication);


    /**
     * This method deletes the user's information.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @DeleteMapping(
            value="/delete",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method deletes the user's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful delete of user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized delete of user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) })
    }
    )
    ResponseEntity<String> deleteUser(Authentication authentication);

}
