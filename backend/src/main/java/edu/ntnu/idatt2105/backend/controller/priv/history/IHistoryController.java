package edu.ntnu.idatt2105.backend.controller.priv.history;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.history.QuizHistoryDTO;
import edu.ntnu.idatt2105.backend.model.quiz.QuizHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This interface outlines the various functionality the private endpoint for history's should have.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
public interface IHistoryController {

    /**
     * This method adds a new historical event for a quiz and user.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PostMapping(
            value="/create/event",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method adds a new historical event for a user and a given quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful addition of event.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizHistory.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized addition of event.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizHistory.class)) })
    }
    )
    ResponseEntity<QuizLoadDTO> addHistoricalEvent(@RequestBody @NonNull QuizHistoryDTO historicalEvent,
                                                   @NonNull Authentication authentication);

    /**
     * This method retrieves all historical events for a given user.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @GetMapping(
            value="/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method retrieves all historical events for a given user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of events.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizHistory.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized retrieval of events.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizHistory.class)) })
    }
    )
    ResponseEntity<QuizLoadDTO> getAllHistoricalEvent(@PathVariable @NonNull Long userId,
                                                   @NonNull Authentication authentication);

}
