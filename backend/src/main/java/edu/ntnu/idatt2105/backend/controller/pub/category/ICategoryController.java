package edu.ntnu.idatt2105.backend.controller.pub.category;

import edu.ntnu.idatt2105.backend.dto.category.MultipleCategoryDTO;
import edu.ntnu.idatt2105.backend.model.category.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This interface outlines the public endpoints for the category.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 05.04.2024
 */
public interface ICategoryController {

    /**
     * This method retrieves all quizzes.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @GetMapping(
            value="/getAll"
    )
    @Operation(summary = "This method retrieves all categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of categories.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)) }),
            @ApiResponse(responseCode = "500", description = "Server error while retrieving categories.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)) })
    }
    )
    ResponseEntity<MultipleCategoryDTO> getAllCategories();

}
