package edu.ntnu.idatt2105.backend.service.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.history.QuizHistoryLoadAllDTO;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizHistoryMapper;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.QuizHistory;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizHistoryRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.util.Player;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuizHistoryService {

    private final QuizHistoryRepository quizHistoryRepository;
    private final AuthenticationService authenticationService;
    /**
     * Adds a quiz history to the database from players of a game.
     *
     * @param players The players of the game.
     * @param quiz The quiz that was played.
     */
    @Transactional
    public void addHistory(List<Player> players, Quiz quiz) {
        for (Player player : players) {
            quizHistoryRepository.save(QuizHistory.builder().quiz(quiz).score(player.getScore()).timestamp(LocalDateTime.now()).user(player.getUser()).build());
        }
    }

    /**
     * Gets all the histories for a user.
     *
     * @return The histories for the user.
     */
    @Transactional
    public ResponseEntity<QuizHistoryLoadAllDTO> getHistoryForUser() {
        Set<QuizHistory> histories = authenticationService.getLoggedInUser().getQuizHistory();
        return ResponseEntity.ok(QuizHistoryMapper.INSTANCE.multipleQuizHistoryToDTO(histories));
    }
}
