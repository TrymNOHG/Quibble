package edu.ntnu.idatt2105.backend.service.websocket;

import edu.ntnu.idatt2105.backend.dto.websocket.SendAlternativesDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.MultipleChoiceRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.JWTTokenService;
import edu.ntnu.idatt2105.backend.service.quiz.QuestionService;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import edu.ntnu.idatt2105.backend.util.Game;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class GameService {
    private final Map<String, Game> rooms = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final JWTTokenService jwtTokenService;
    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    Logger logger = Logger.getLogger(GameService.class.getName());

    /**
     * Generates a random code for a room that is not already taken. If the code is taken, it generates a new one.
     *
     * @return A randomly generated code that is not already in the rooms map.
     */
    private String generateRandomCode() {
        String code;
        do {
            code = random.ints(48, 123)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(4) // Change this to increase the length of the code
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } while (rooms.containsKey(code));
        return code;
    }

    @Transactional
    public String createGame(String hostEmail, long quizId, UUID hostUUID) {
        logger.info("Creating game with host email: " + hostEmail);
        User host = userRepository.findByEmail(hostEmail).orElseThrow();
        String code = generateRandomCode();
        logger.info("Creating game with code: " + code);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        for (Question q : quiz.getQuestions()) {
            logger.info(q.getQuestion());
        }
        Game game = new Game(code, host, quiz, quiz.getQuestions().stream().toList(), hostUUID);
        rooms.put(code, game);
        return code;
    }

    @Transactional
    public boolean isGameHost(String code, String jwt) {
        String email = jwtTokenService.getUserEmail(jwt);
        Game game = rooms.get(code);
        return game.getHost().getEmail().equals(email);
    }

    public Game getGame(String code) {
        return rooms.get(code);
    }

    public void removeGame(String code) {
        rooms.remove(code);
    }

    public boolean deleteGameFromUUID(UUID uuid) {
        return rooms.entrySet().removeIf(entry -> entry.getValue().getHostUUID().equals(uuid));
    }

    public boolean deleteAnonUserFromGame(UUID uuid) {
        return rooms.values().stream().anyMatch(game -> game.getAnonymousPlayers().remove(uuid) != null);
    }

    public SendAlternativesDTO getAlternatives(String code) {//TODO: flytt til QuestionService for å fikse error
        Game game = rooms.get(code);
        Question question = game.getQuestions().get(game.getQuestionIndex());
        return questionService.getAlternatives(question.getQuestionId());
    }

    public void beginAnsweringTimer(String code) {
        Game game = rooms.get(code);
        game.startTimer();
    }
}
