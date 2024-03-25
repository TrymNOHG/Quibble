package edu.ntnu.idatt2105.backend.controller.priv.history;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.model.quiz.QuizHistory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides the private endpoint for history.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@RestController
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/private/history")
public class HistoryController implements IHistoryController{
    @Override
    public ResponseEntity<QuizLoadDTO> addHistoricalEvent(@NonNull QuizHistory historicalEvent, @NonNull Authentication authentication) {
        return null;
    }

    @Override
    public ResponseEntity<QuizLoadDTO> getAllHistoricalEvent(@NonNull Long userId, @NonNull Authentication authentication) {
        // Could add a check of user preferences to see if historical events should be viewable.
        return null;
    }
}
