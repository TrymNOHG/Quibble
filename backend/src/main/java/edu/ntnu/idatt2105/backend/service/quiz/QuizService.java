package edu.ntnu.idatt2105.backend.service.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public Quiz getQuizById(long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz with id " + quizId + " not found"));
    }

    @Transactional
    public QuizLoadDTO createQuiz(String quizName, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + adminEmail + " not found"));
        Quiz quiz = Quiz.builder()
                .quizName(quizName)
                .admin(admin)
                .build();
        quiz = quizRepository.save(quiz);
        return QuizLoadDTO.builder()
                .quizId(quiz.getQuizId())
                .quizName(quiz.getQuizName())
                .admin_id(admin.getUserId())
                .build();
    }
}
