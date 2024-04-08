package edu.ntnu.idatt2105.backend.controller.priv.statistics;

import edu.ntnu.idatt2105.backend.dto.quiz.history.QuizHistoryLoadAllDTO;
import edu.ntnu.idatt2105.backend.service.quiz.QuizHistoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller contains the endpoints for handling private statistics information.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.0 05.04.2024
 */
@RestController("statisticsController")
@EnableAutoConfiguration
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping(value = "/api/v1/private/stat")
public class StatisticsController implements IStatisticsController {
    private final QuizHistoryService quizHistoryService;

    @Override
    public ResponseEntity<QuizHistoryLoadAllDTO> getStatisticsForUser(@NonNull Authentication authentication) {
        return quizHistoryService.getHistoryForUser();
    }
}
