package edu.ntnu.idatt2105.backend.controller.priv.feedback;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackUpdateDTO;
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
 * This controller provides the private endpoint for feedback.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@RestController("privateFeedbackController")
@EnableAutoConfiguration
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping(value = "/api/v1/private/feedback")
public class FeedbackController implements IFeedbackController {
    @Override
    public ResponseEntity<QuizLoadDTO> addFeedback(@NonNull QuizFeedbackDTO newFeedback, @NonNull Authentication authentication) {
        return null;
    }

    @Override
    public ResponseEntity<QuizLoadDTO> updateFeedback(@NonNull QuizFeedbackUpdateDTO updatedFeedback, @NonNull Authentication authentication) {
        return null;
    }

    @Override
    public ResponseEntity<QuizLoadDTO> deleteFeedback(@NonNull Long quizId, @NonNull Authentication authentication) {
        return null;
    }
}
