package edu.ntnu.idatt2105.backend.controller.priv.users;

import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserUpdateDTO;
import edu.ntnu.idatt2105.backend.model.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.FileSystemException;

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
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
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
    ResponseEntity<UserLoadDTO> updateUser(@ModelAttribute @NonNull UserUpdateDTO userUpdateDTO, @NonNull Authentication authentication) throws FileSystemException;


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

    /**
     * This method retrieves a given user's own information.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @GetMapping(
            value="/get"
    )
    @Operation(summary = "This method retrieves the user's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized retrieval of user.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) })
    }
    )
    ResponseEntity<Object> getUser(Authentication authentication);

//    @PatchMapping(
//            value="/update/showActivity",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @Operation(summary = "This method updates the user's show activity preference.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful update of user.",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = User.class)) }),
//            @ApiResponse(responseCode = "403", description = "Unauthorized update of user.",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = User.class)) })
//    }
//    )
//    ResponseEntity<Object> updateUserShowActivity(@RequestBody boolean newShowActivity, @NonNull Authentication authentication) throws FileSystemException;
//
//    @PatchMapping(
//            value="/update/showFeedback",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @Operation(summary = "This method updates the user's show feedback preference.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful update of user.",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = User.class)) }),
//            @ApiResponse(responseCode = "403", description = "Unauthorized update of user.",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = User.class)) })
//    }
//    )
//    ResponseEntity<Object> updateUserShowFeedback(@RequestBody boolean newShowFeedback, @NonNull Authentication authentication) throws FileSystemException;
//


}
