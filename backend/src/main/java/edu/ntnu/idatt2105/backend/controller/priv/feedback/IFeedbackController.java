package edu.ntnu.idatt2105.backend.controller.priv.feedback;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackUpdateDTO;
import edu.ntnu.idatt2105.backend.model.quiz.QuizFeedback;
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

/**
 * This interface outlines the various functionality the private endpoint for feedback should have.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
public interface IFeedbackController {

    /**
     * This method adds a new feedback to a quiz.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PostMapping(
            value="/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method adds a new feedback to a given quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful addition of feedback.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizFeedback.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized addition of feedback.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizFeedback.class)) })
    }
    )
    ResponseEntity<String> addFeedback(@RequestBody @NonNull QuizFeedbackDTO newFeedback,
                                                @NonNull Authentication authentication);

    /**
     * This method updates the feedback for a quiz.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PatchMapping(
            value="/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method updates the feedback for a quiz.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful update of feedback.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizFeedback.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized update of feedback.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizFeedback.class)) })
    }
    )
    ResponseEntity<QuizFeedbackLoadDTO> updateFeedback(@RequestBody @NonNull QuizFeedbackUpdateDTO updatedFeedback,
                                                       @NonNull Authentication authentication);

    /**
     * This method deletes the feedback for a quiz.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @DeleteMapping(
            value="/delete/{quizId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method deletes the feedback for a quiz.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deletion of feedback.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizFeedback.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized deletion of feedback.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizFeedback.class)) })
    }
    )
    ResponseEntity<String> deleteFeedback(@PathVariable @NonNull Long quizId,
                                               @NonNull Authentication authentication);

    /**
     * This method gets the feedback for a quiz.
     *
     * @param quizId The id of the quiz.
     * @param authentication The authentication object.
     * @return ResponseEntity showing whether the operation was successful.
     */
    @GetMapping(
            value="/get/{quizId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method gets the feedback for a quiz.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of feedback.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizFeedback.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized retrieval of feedback.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizFeedback.class)) })
    }
    )
    ResponseEntity<QuizFeedbackLoadAllDTO> getFeedbacks(@PathVariable @NonNull Long quizId,
                                                        @NonNull Authentication authentication);
}
