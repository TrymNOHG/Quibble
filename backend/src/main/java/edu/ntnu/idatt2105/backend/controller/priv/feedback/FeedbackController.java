package edu.ntnu.idatt2105.backend.controller.priv.feedback;

import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackUpdateDTO;
import edu.ntnu.idatt2105.backend.service.quiz.FeedbackService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final FeedbackService feedbackService;

    @Override
    public ResponseEntity<String> addFeedback(@NonNull QuizFeedbackDTO newFeedback, @NonNull Authentication authentication) {
        return new ResponseEntity<>(feedbackService.addFeedback(newFeedback), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<QuizFeedbackLoadDTO> updateFeedback(@NonNull QuizFeedbackUpdateDTO updatedFeedback, @NonNull Authentication authentication) {
        return new ResponseEntity<>(feedbackService.updateFeedback(updatedFeedback), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteFeedback(@NonNull Long quizId, @NonNull Authentication authentication) {
        return new ResponseEntity<>(feedbackService.removeFeedback(quizId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QuizFeedbackLoadAllDTO> getFeedbacks(@PathVariable @NonNull Long quizId,
                                                               @NonNull Authentication authentication) {
        return new ResponseEntity<>(feedbackService.getFeedbacks(quizId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QuizFeedbackLoadAllDTO> getOwnFeedback(@NonNull Authentication authentication) {
        log.info("Getting own feedback.");
        return new ResponseEntity<>(feedbackService.getOwnFeedback(authentication), HttpStatus.OK);
    }
}
