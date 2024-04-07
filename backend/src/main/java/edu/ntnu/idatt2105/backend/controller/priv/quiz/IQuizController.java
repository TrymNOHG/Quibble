package edu.ntnu.idatt2105.backend.controller.priv.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryCreateMultDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryLoadMultDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionEditDTO;
import edu.ntnu.idatt2105.backend.model.category.QuizCategory;
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

import java.io.IOException;

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
                                           @NonNull Authentication authentication) throws IOException;

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
            value="/delete/{quizId}"
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
    ResponseEntity<QuizLoadDTO> deleteQuiz(@PathVariable @NonNull Long quizId,
                                           @NonNull Authentication authentication);

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
    ResponseEntity<QuizAuthorLoadDTO> addCollaborator(@RequestBody @NonNull QuizAuthorDTO newCollaborator,
                                                      @NonNull Authentication authentication);

    /**
     * This method removes a collaborator from a quiz.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @DeleteMapping(
            value="/delete/collab",
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
    ResponseEntity<Object> removeCollaborator(@RequestBody @NonNull QuizAuthorDTO removeCollaborator,
                                                @NonNull Authentication authentication);

    @DeleteMapping(
            value="/delete/collaborator/{authorId}"
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
    ResponseEntity<Object> removeCollaborator(@PathVariable @NonNull Long authorId,
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
    ResponseEntity<QuizLoadDTO> addQuestion(@RequestBody @NonNull QuestionCreateDTO questionCreateDTO, @NonNull Authentication authentication);

    @PatchMapping(
            value="/edit/question",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method edits a question of a given quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful edit of question.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizAuthor.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized edit of question.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizAuthor.class)) })
    }
    )
    ResponseEntity<QuizLoadDTO> editQuestion(@RequestBody @NonNull QuestionEditDTO questionEditDTO, @NonNull Authentication authentication);

    @DeleteMapping(
            value="/delete/question/{questionId}"
    )
    @Operation(summary = "This method deletes a question of a given quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deletion of question.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizAuthor.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized deletion of question.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizAuthor.class)) })
    }
    )
    ResponseEntity<Object> deleteQuestion(@PathVariable @NonNull Long questionId, @NonNull Authentication authentication);


    /**
     * This method adds a new category to a quiz.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PostMapping(
            value="/create/category",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method adds a new category to a given quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful addition of category.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizCategory.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized addition of category.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizCategory.class)) })
    }
    )
    ResponseEntity<QuizCategoryLoadDTO> addCategory(@RequestBody @NonNull QuizCategoryCreateDTO quizCategoryCreateDTO, @NonNull Authentication authentication);

    /**
     * This method adds a new category to a quiz.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PostMapping(
            value="/create/categories",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "This method adds a new categories to a given quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful addition of categories.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizCategory.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized addition of categories.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizCategory.class)) })
    }
    )
    ResponseEntity<QuizCategoryLoadMultDTO> addCategories(@RequestBody @NonNull QuizCategoryCreateMultDTO quizCategoryCreateMultDTO, @NonNull Authentication authentication);


    @DeleteMapping(
            value="/delete/category/{quizCategoryId}"
    )
    @Operation(summary = "This method deletes a category of a given quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deletion of category.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizCategory.class)) }),
            @ApiResponse(responseCode = "403", description = "Unauthorized deletion of category.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizCategory.class)) })
    }
    )
    ResponseEntity<Object> deleteQuizCategory(@PathVariable @NonNull Long quizCategoryId, @NonNull Authentication authentication);


}