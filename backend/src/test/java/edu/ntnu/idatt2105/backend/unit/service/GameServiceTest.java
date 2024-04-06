package edu.ntnu.idatt2105.backend.unit.service;

import edu.ntnu.idatt2105.backend.dto.websocket.SendAlternativesDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.MultipleChoiceRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.security.JWTTokenGenerationService;
import edu.ntnu.idatt2105.backend.service.websocket.GameService;
import edu.ntnu.idatt2105.backend.util.Game;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Slf4j
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTTokenGenerationService jwtTokenGenerationService;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MultipleChoiceRepository multipleChoiceRepository;


    private User user;
    private Quiz quiz;

    private void addUserToDatabase() {
        // Setup logic here
        user = User.builder()
                .username("username")
                .email("email@email.email")
                .password("password")
                .build();
        userRepository.save(user);
    }

    @Transactional
    void addQuizToDatabase(User user) {
        Set<MultipleChoice> choices = Set.of(
                MultipleChoice.builder().alternative("Oslo").isCorrect(true).build(),
                MultipleChoice.builder().alternative("Bergen").isCorrect(false).build(),
                MultipleChoice.builder().alternative("Trondheim").isCorrect(false).build(),
                MultipleChoice.builder().alternative("Stavanger").isCorrect(false).build()
        );
        Question question1 = Question.builder()
                .question("What is the capital of Norway?")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .answer("Oslo")
                .quiz(quiz)
                .choices(choices)
                .build();
        Quiz newQuiz = Quiz.builder()
                .quizName("Capitals of Scandinavia")
                .quizDescription("A quiz about the capitals of Scandinavia")
                .admin(user)
                .difficulty(Difficulty.EASY)
                .questions(Set.of(question1))
                .build();
        Quiz quiz = quizRepository.save(newQuiz);

        questionRepository.save(question1);

        multipleChoiceRepository.saveAll(choices);
        this.quiz = quizRepository.findById(quiz.getQuizId()).get();
    }

    @Transactional
    String generateGame() {
        addUserToDatabase();
        UUID uuid = UUID.randomUUID();
        addQuizToDatabase(user);
        return gameService.createGame(user.getEmail(), quiz.getQuizId(), uuid);
    }

    @Transactional
    String generateGame(UUID uuid) {
        addUserToDatabase();
        addQuizToDatabase(user);
        return gameService.createGame(user.getEmail(), quiz.getQuizId(), uuid);
    }

    String generateAccessToken(User user) {
        return jwtTokenGenerationService.generateToken(user.getEmail());
    }

    @Test
    void Generate_random_code_and_create_game_test() {
        addUserToDatabase();
        addQuizToDatabase(user);
        UUID uuid = UUID.randomUUID();

        String code = gameService.createGame(user.getEmail(), quiz.getQuizId(), uuid);
        assertNotNull(code);
        assertEquals(4, code.length());

        assertNotNull(gameService.getGame(code));
    }

    @Test
    void Is_game_host_test() {
        String code = generateGame();
        assertTrue(gameService.isGameHost(code, generateAccessToken(user)));
    }

    @Test
    void Get_game_test() {
        String code = generateGame();
        assertNotNull(gameService.getGame(code));
    }

    @Test
    void Remove_game_test() {
        String code = generateGame();
        assertNotNull(gameService.getGame(code));
        gameService.removeGame(code);
        assertNull(gameService.getGame(code));
    }

    @Test
    void Delete_game_from_uuid_test() {
        UUID uuid = UUID.randomUUID();
        String code = generateGame(uuid);
        assertNotNull(gameService.getGame(code));
        gameService.deleteGameFromUUID(uuid);
        assertNull(gameService.getGame(code));
    }

    @Test
    void Delete_anonymous_user_from_game_test() {
        UUID uuid = UUID.randomUUID();
        String code = generateGame();
        gameService.getGame(code).addPlayer(uuid, "testPlayer", "default-1");
        assertTrue(gameService.getGame(code).getAnonymousPlayers().containsKey(uuid));
        gameService.deleteAnonUserFromGame(uuid);
        assertFalse(gameService.getGame(code).getAnonymousPlayers().containsKey(uuid));
    }

    @Test
    @Transactional
    void Get_alternatives_test() {
        String code = generateGame();
        gameService.getGame(code).startGame();
        SendAlternativesDTO alternatives = gameService.getAlternatives(code);
        assertNotNull(alternatives);
        assertNotNull(alternatives.questionType());
        assertNotNull(alternatives.alternatives());
        assertEquals(4, alternatives.alternatives().length);
    }

    @Test
    void Begin_answering_timer_test() {
        String code = generateGame();
        Game game = gameService.getGame(code);
        game.startGame();
        gameService.beginAnsweringTimer(code);
        assertTrue(gameService.getGame(code).getAnswerStart().containsKey(game.getQuestionIndex()));
    }

}