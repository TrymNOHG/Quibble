package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackUpdateDTO;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizFeedbackMapperImpl;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.QuizFeedback;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizFeedbackRepository;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import edu.ntnu.idatt2105.backend.service.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class FeedbackService {

    private final QuizFeedbackRepository quizFeedbackRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final QuizService quizService;

    @Transactional
    public String addFeedback(QuizFeedbackDTO newFeedback) {
        log.info("Adding feedback.");
        authenticationService.verifyUserId(newFeedback.userId());
        log.info("User verified.");
        if (newFeedback.stars() < 1 || newFeedback.stars() > 5) {
            log.info("Stars must be between 1 and 5.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Stars must be between 1 and 5.");
        }
        Quiz quiz = quizService.getQuizById(newFeedback.quizId());
        User user = authenticationService.getLoggedInUser();
        quizFeedbackRepository.save(QuizFeedback.builder()
                .feedback(newFeedback.feedback())
                .stars(newFeedback.stars())
                .quiz(quiz)
                .user(user)
                .build());
        log.info("Successfully added feedback.");
        return "Successfully added feedback.";
    }

    /**
     * Removes a feedback. The user must be the author of the feedback.
     *
     * @param feedbackId The id of the feedback.
     * @return A message confirming the removal.
     */
    @Transactional
    public String removeFeedback(long feedbackId) {
        quizFeedbackRepository.deleteById(feedbackId);
        return "Successfully removed feedback.";
    }

    /**
     * Updates a feedback. The user must be the author of the feedback.
     *
     * @param updatedFeedback The updated feedback.
     * @return The updated feedback.
     */
    @Transactional
    public QuizFeedbackLoadDTO updateFeedback(QuizFeedbackUpdateDTO updatedFeedback) {
        log.info("Updating feedback.");
        QuizFeedback feedback = quizFeedbackRepository.findById(updatedFeedback.feedbackId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found."));
        authenticationService.verifyUserId(feedback.getUser().getUserId());
        log.info("User verified.");
        if (updatedFeedback.stars() < 1 || updatedFeedback.stars() > 5) {
            log.info("Stars must be between 1 and 5.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Stars must be between 1 and 5.");
        }
        feedback.setFeedback(updatedFeedback.feedback());
        feedback.setStars(updatedFeedback.stars());
        QuizFeedback newFeedback = quizFeedbackRepository.save(feedback);
        log.info("Successfully updated feedback.");
        return QuizFeedbackMapperImpl.INSTANCE.quizFeedbackToQuizFeedbackLoadDTO(newFeedback);
    }

    /**
     * Gets all feedback from a quiz. The owner or collaborator can se all feedback, while other users can only see
     * public feedback and their own feedback.
     *
     * @param quizId The id of the quiz.
     * @return A list of feedback.
     */
    @Transactional
    public QuizFeedbackLoadAllDTO getFeedbacks(long quizId) {
        log.info("Getting feedbacks.");
        Quiz quiz = quizService.getQuizById(quizId);
        long userId = authenticationService.getLoggedInUserId();
        if (authenticationService.getLoggedInUserId() == quizService.getQuizById(quizId).getAdmin().getUserId()
                || quizService.getAllCollaborators(quizId).stream().anyMatch(user -> user.getUser().getUserId() == userId)){
            log.info("User is owner or collaborator, returning all feedbacks for the quiz");
            return QuizFeedbackLoadAllDTO.builder()
                    .feedbacks(quiz.getFeedbacks().stream()
                            .map(QuizFeedbackMapperImpl.INSTANCE::quizFeedbackToQuizFeedbackLoadDTO)
                            .collect(Collectors.toSet()))
                    .build();
        }

        log.info("User is not owner or collaborator, only return public feedbacks and the user's own feedback.");
        Set<QuizFeedback> feedbacks = quiz.getFeedbacks();
        return QuizFeedbackLoadAllDTO.builder()
                .feedbacks(feedbacks.stream()
                        .filter(feedback -> feedback.getUser().getUserId() == userId || feedback.getUser().isShowFeedback())
                        .map(QuizFeedbackMapperImpl.INSTANCE::quizFeedbackToQuizFeedbackLoadDTO)
                        .collect(Collectors.toSet()))
                .build();
    }
}
