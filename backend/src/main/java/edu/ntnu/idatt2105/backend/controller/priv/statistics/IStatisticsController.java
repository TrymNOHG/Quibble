package edu.ntnu.idatt2105.backend.controller.priv.statistics;

import edu.ntnu.idatt2105.backend.dto.quiz.history.QuizHistoryLoadAllDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This interface provides the private endpoint for statistics.
 *
 * @version 1.0 06.03.2024
 * @author Brage Halvorsen Kvamme
 */
public interface IStatisticsController {

    @GetMapping(value = "/user")
    @Operation(summary = "This method gets the statistics for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics for user."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")
    }
    )
    ResponseEntity<QuizHistoryLoadAllDTO> getStatisticsForUser(@NonNull Authentication authentication);
}
