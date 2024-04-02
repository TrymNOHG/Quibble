package edu.ntnu.idatt2105.backend.controller.pub.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides the public endpoint for quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@RestController("publicQuizController")
@EnableAutoConfiguration
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping(value = "/api/v1/public/quiz")
public class QuizController implements IQuizController{

    private final QuizService quizService;

    @Override
    public ResponseEntity<QuizLoadAllDTO> getAllQuizzes() {
        QuizLoadAllDTO quizLoadAllDTO = quizService.loadAllQuiz();
        return ResponseEntity.ok(quizLoadAllDTO);
    }

    @Override
    public ResponseEntity<Page<QuizLoadDTO>> getQuizzes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QuizLoadDTO> quizLoadPage = quizService.getPage(pageable);
        return ResponseEntity.ok(quizLoadPage);
    }
}
