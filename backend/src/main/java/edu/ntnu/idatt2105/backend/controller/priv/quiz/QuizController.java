package edu.ntnu.idatt2105.backend.controller.priv.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryCreateMultDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryLoadMultDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionEditDTO;
import edu.ntnu.idatt2105.backend.service.quiz.QuestionService;
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
    private final QuestionService questionService;
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
        logger.info("Deleting quiz with ID: {}" + quizId);
        quizService.deleteQuiz(quizId);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<QuizAuthorLoadDTO> addCollaborator(@NonNull QuizAuthorDTO newCollaborator, @NonNull Authentication authentication) {
        logger.info("adding collaborator");
        //TODO: check that user is owner or collaborator
        // Check if already part of quiz
        // Check is authorized
        // Check is admin? exists? etc.
        // Other edge cases...
        return ResponseEntity.ok(quizService.addCollaborator(newCollaborator));
    }

    @Override
    public ResponseEntity<Object> removeCollaborator(@NonNull QuizAuthorDTO removeCollaborator, @NonNull Authentication authentication) {
        logger.info("deleting collaborator");
        //TODO: check that user is owner or collaborator
        quizService.removeCollaborator(removeCollaborator);
        return ResponseEntity.ok("Deletion was successful");
    }

    @Override
    public ResponseEntity<Object> removeCollaborator(@NonNull Long authorId, @NonNull Authentication authentication) {
        logger.info("deleting collaborator");
        //TODO: check that user is owner or collaborator
        quizService.removeCollaborator(authorId);
        return ResponseEntity.ok("Deletion was successful");
    }

    @Override
    public ResponseEntity<QuizLoadDTO> addQuestion(@NonNull QuestionCreateDTO questionCreateDTO, @NonNull Authentication authentication) {
        //TODO: check that user is owner or collaborator
        //TODO: make method that checks if user is owner or collaborator of quiz.
        QuizLoadDTO quizLoadDTO = questionService.addQuestion(questionCreateDTO);
        return ResponseEntity.ok(quizLoadDTO);
    }

    @Override
    public ResponseEntity<QuizLoadDTO> editQuestion(@NonNull QuestionEditDTO questionEditDTO, @NonNull Authentication authentication) {
        QuizLoadDTO quizLoadDTO = questionService.editQuestion(questionEditDTO);
        return ResponseEntity.ok(quizLoadDTO);
    }

    @Override
    public ResponseEntity<Object> deleteQuestion(@NonNull Long questionId, @NonNull Authentication authentication) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok("Successful Deletion");
    }

    @Override
    public ResponseEntity<QuizCategoryLoadDTO> addCategory(@NonNull QuizCategoryCreateDTO quizCategoryCreateDTO, @NonNull Authentication authentication) {
        QuizCategoryLoadDTO quizCategoryLoadDTO = quizService.addQuizCategory(quizCategoryCreateDTO);
        return ResponseEntity.ok(quizCategoryLoadDTO);
    }

    @Override
    public ResponseEntity<QuizCategoryLoadMultDTO> addCategories(@NonNull QuizCategoryCreateMultDTO quizCategoryCreateMultDTO, @NonNull Authentication authentication) {
        QuizCategoryLoadMultDTO quizCategoryLoadMultDTO = quizService.addQuizCategories(quizCategoryCreateMultDTO);
        return ResponseEntity.ok(quizCategoryLoadMultDTO);
    }

    @Override
    public ResponseEntity<Object> deleteQuizCategory(@NonNull Long quizCategoryId, @NonNull Authentication authentication) {
        quizService.removeQuizCategory(quizCategoryId);
        return ResponseEntity.ok("Successful Deletion");
    }


}
