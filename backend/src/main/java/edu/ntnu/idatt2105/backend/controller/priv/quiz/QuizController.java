package edu.ntnu.idatt2105.backend.controller.priv.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorDTO;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * This controller provides the private endpoint for quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@RestController("privateQuizController")
@EnableAutoConfiguration
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping(value = "/api/v1/private/quiz")
public class QuizController implements IQuizController{

    private final QuizService quizService;
    Logger logger = Logger.getLogger(QuizController.class.getName());

    @Override
    public ResponseEntity<QuizLoadDTO> createQuiz(@NonNull String quizName, @NonNull Authentication authentication) {
        logger.info("Authenicating user: " + authentication.getName());
        return ResponseEntity.ok(quizService.createQuiz(quizName, authentication.getName()));
    }

    @Override
    public ResponseEntity<QuizLoadDTO> updateQuiz(@NonNull QuizUpdateDTO quizUpdateDTO, @NonNull Authentication authentication) {
        QuizLoadDTO quizLoadDTO = quizService.updateQuiz(quizUpdateDTO);
        return ResponseEntity.ok(quizLoadDTO);
    }

    @Override
    public ResponseEntity<QuizLoadDTO> deleteQuiz(@NonNull Long quizId, @NonNull Authentication authentication) {
        return null;
    }

    @Override
    public ResponseEntity<QuizLoadDTO> addCollaborator(@NonNull QuizAuthorDTO newCollaborator, @NonNull Authentication authentication) {
        // Check if already part of quiz
        // Check is authorized
        // Check is admin? exists? etc.
        // Other edge cases...
        return null;
    }

    @Override
    public ResponseEntity<QuizLoadDTO> removeCollaborator(@NonNull Long userId, @NonNull Authentication authentication) {
        return null;
    }


}
