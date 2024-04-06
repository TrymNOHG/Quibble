package edu.ntnu.idatt2105.backend.controller.pub.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizFilterDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This interface outlines the various functionality the public endpoint for quiz should have.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
public interface IQuizController {

    // Load quiz, Load multiple quiz by search, load collaborators, Load single question
    /**
     * This method retrieves all quizzes.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @GetMapping(
            value="/getAll"
    )
    @Operation(summary = "This method retrieves all quizzes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of quizzes.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) }),
            @ApiResponse(responseCode = "500", description = "Server error while retrieving quizzes.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) })
    }
    )
    ResponseEntity<QuizLoadAllDTO> getAllQuizzes();

    /**
     * This method retrieves a page of quizzes.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @GetMapping(
            value="/get"
    )
    @Operation(summary = "This method retrieves a page of quizzes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of quizzes.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) }),
            @ApiResponse(responseCode = "500", description = "Server error while retrieving quizzes.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) })
    }
    )
    ResponseEntity<Page<QuizLoadDTO>> getQuizzes(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size);


    /**
     * This method retrieves a page of quizzes.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @PostMapping(
            value="/getFiltered"
    )
    @Operation(summary = "This method retrieves a page of quizzes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of quizzes.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) }),
            @ApiResponse(responseCode = "500", description = "Server error while retrieving quizzes.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Quiz.class)) })
    }
    )
    ResponseEntity<Page<QuizLoadDTO>> getQuizzes(@RequestBody @NonNull QuizFilterDTO quizFilterDTO);


}
