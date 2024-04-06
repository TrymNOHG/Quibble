package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackDTO;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizFeedbackMapperImpl;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizFeedbackRepository;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final QuizFeedbackRepository quizFeedbackRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Transactional
    public String addFeedback(QuizFeedbackDTO newFeedback) {
        authenticationService.verifyUserId(newFeedback.userId());
        if (newFeedback.stars() < 1 || newFeedback.stars() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Stars must be between 1 and 5.");
        }
//        quizFeedbackRepository.save(QuizFeedbackMapperImpl.INSTANCE.quizFeedbackLoadDTOToQuizFeedback(newFeedback));
        return "Successfully added feedback.";
    }

}
