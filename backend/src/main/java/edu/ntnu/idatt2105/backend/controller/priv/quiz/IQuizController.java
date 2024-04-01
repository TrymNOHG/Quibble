package edu.ntnu.idatt2105.backend.controller.priv.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
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
 * This interface outlines the various functionality the private endpoint for quiz's should have.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
public interface IQuizController {

    /**
     * This method creates a new quiz.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PostMapping(
            value="/create/{quizName}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method creates a new quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful creation of quiz.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized creation of quiz.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) })
    }
    )
    ResponseEntity<QuizLoadDTO> createQuiz(@PathVariable @NonNull String quizName,
                                           @NonNull Authentication authentication);

    @PatchMapping(
            value="/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method updates a quiz given its id. This is useful for example adding, changing, or " +
            "deleting questions and editing the quiz name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful creation of quiz.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized creation of quiz.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) })
    }
    )
    ResponseEntity<QuizLoadDTO> updateQuiz(@RequestBody @NonNull QuizUpdateDTO quizUpdateDTO, @NonNull Authentication authentication);

    @DeleteMapping(
            value="/delete/{quizId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method deletes a quiz given its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deletion of quiz.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized deletion of quiz.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) })
    }
    )
    ResponseEntity<QuizLoadDTO> deleteQuiz(@PathVariable @NonNull Long quizId, @NonNull Authentication authentication);

    /**
     * This method adds a new collaborator to a quiz.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PostMapping(
            value="/create/collaborator",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method adds a new collaborator to a given quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful addition of collaborator.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizAuthor.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized addition of collaborator.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizAuthor.class)) })
    }
    )
    ResponseEntity<QuizLoadDTO> addCollaborator(@RequestBody @NonNull QuizAuthorDTO newCollaborator,
                                                @NonNull Authentication authentication);

    /**
     * This method removes a collaborator from a quiz.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @DeleteMapping(
            value="/delete/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method removes a collaborator from a quiz.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful removal of collaborator.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizAuthor.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized removal of collaborator.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizAuthor.class)) })
    }
    )
    ResponseEntity<QuizLoadDTO> removeCollaborator(@PathVariable @NonNull Long userId,
                                                @NonNull Authentication authentication);

    /**
     * This method adds a new question to a quiz.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PostMapping(
            value="/create/question",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method adds a new question to a given quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful addition of question.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizAuthor.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized addition of question.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizAuthor.class)) })
    }
    )
    ResponseEntity<QuizLoadDTO> addQuestion(@NonNull QuestionCreateDTO questionCreateDTO, @NonNull Authentication authentication);

}