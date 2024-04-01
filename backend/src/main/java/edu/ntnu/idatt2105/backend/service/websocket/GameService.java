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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Service class for managing different instances of the Game class.
 *
 * @version 1.0 31.03.2021
 * @author Brage Halvorsen Kvamme
 * @see Game
 */
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

    /**
     * Creates a game with a host, a quiz and a code. The game is then added to the rooms map.
     *
     * @param hostEmail The email of the host of the game.
     * @param quizId The id of the quiz that the game is playing.
     * @param hostUUID The UUID of the host of the game.
     * @return The code of the game that was created.
     */
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

    /**
     * Adds a player to a game. If the player is already in the game, the player is not added.
     *
     * @param code The code of the game that the player is to be added to.
     * @param jwt The JWT of the player that is to be added.
     * @return True if the player was added, false if the player was already in the game.
     */
    @Transactional
    public boolean isGameHost(String code, String jwt) {
        String email = jwtTokenService.getUserEmail(jwt);
        Game game = rooms.get(code);
        return game.getHost().getEmail().equals(email);
    }

    /**
     * Gets the game from the game code.
     *
     * @param code The code of the game.
     * @return The game with the given code.
     */
    public Game getGame(String code) {
        return rooms.get(code);
    }

    /**
     * Removes a game from available games.
     *
     * @param code The code of the game to be removed.
     */
    public void removeGame(String code) {
        rooms.remove(code);
    }

    /**
     * Deletes a game based on the UUID of the host.
     *
     * @param uuid The UUID of the host of the game to be deleted.
     * @return True if the game was deleted, false if the game was not found.
     */
    public boolean deleteGameFromUUID(UUID uuid) {
        return rooms.entrySet().removeIf(entry -> entry.getValue().getHostUUID().equals(uuid));
    }

    /**
     * Deletes an anonymous user from a game.
     *
     * @param uuid The UUID of the anonymous user to be deleted.
     * @return True if the user was deleted, false if the user was not found.
     */
    public boolean deleteAnonUserFromGame(UUID uuid) {
        return rooms.values().stream().anyMatch(game -> game.getAnonymousPlayers().remove(uuid) != null);
    }

    /**
     * Gets the alternatives for a question. The question is the current question in the game with the given code.
     *
     * @param code The code of the game.
     * @return The alternatives for the current question in the game.
     */
    public SendAlternativesDTO getAlternatives(String code) {
        Game game = rooms.get(code);
        Question question = game.getQuestions().get(game.getQuestionIndex());
        return questionService.getAlternatives(question.getQuestionId());
    }

    /**
     * Marks the time that a question started. This is used to calculate how long a player has to answer a question.
     *
     * @param code The code of the game.
     */
    public void beginAnsweringTimer(String code) {
        Game game = rooms.get(code);
        game.startTimer();
    }
}
