package edu.ntnu.idatt2105.backend.unit.service;


import com.corundumstudio.socketio.*;
import edu.ntnu.idatt2105.backend.dto.websocket.CreateGameDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.JoinGameDTO;
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
import edu.ntnu.idatt2105.backend.service.security.JWTTokenService;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import edu.ntnu.idatt2105.backend.service.quiz.QuestionService;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import edu.ntnu.idatt2105.backend.service.websocket.GameService;
import edu.ntnu.idatt2105.backend.service.websocket.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Slf4j
public class WebsocketTest {

    @Autowired
    private GameService gameService;
    @Autowired
    private JWTTokenService jwtTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private Environment env;
    @Autowired
    private JWTTokenGenerationService jwtTokenGenerationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MultipleChoiceRepository multipleChoiceRepository;

    @MockBean
    private SocketIOServer server;
    @MockBean
    private SocketIOClient client;
    @MockBean
    private AckRequest ackRequest;

    private SocketService websocketService;
    private String jwt;
    private UUID uuid;
    private User user;
    private final AtomicInteger reachedGameCreated = new AtomicInteger();
    private final AtomicReference<String> code = new AtomicReference<>();
    private final AtomicInteger reachedGameJoined = new AtomicInteger();



    // This took way to long haha
    @BeforeEach
    public void setUp() {
        // Set up websocket service with mocked websocket server
        websocketService = new SocketService(
                server, gameService, jwtTokenService, userService, questionService, env, imageService
        );

        user = User.builder()
                .username("testMister")
                .email("testMister@testMister")
                .password("password")
                .build();
        userRepository.save(user);

        jwt = jwtTokenGenerationService.generateToken(user.getEmail());

        uuid = UUID.randomUUID();

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
                .choices(choices)
                .build();
        Quiz newQuiz = Quiz.builder()
                .quizName("Capitals of Scandinavia")
                .quizDescription("A quiz about the capitals of Scandinavia")
                .admin(user)
                .difficulty(Difficulty.EASY)
                .questions(Set.of(question1))
                .build();
        quizRepository.save(newQuiz);

        questionRepository.save(question1);

        multipleChoiceRepository.saveAll(choices);

        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Object arg1 = invocation.getArgument(1);
            if ("invalidToken".equals(arg0) && "Invalid token".equals(arg1)) {
                // Fail the test if this method is called with these arguments
                fail("sendEvent was called with invalidToken and Invalid token");
            }
            if("gameCreated".equals(arg0) && arg1 instanceof String && ((String) arg1).length() == 4) {
                reachedGameCreated.getAndIncrement();
                code.set((String) arg1);
            }
            if("gameJoined".equals(arg0) && arg1 instanceof String) {
                log.info("Game joined: " + arg1);
                reachedGameJoined.getAndIncrement();
            }
            return null;
        }).when(client).sendEvent(anyString(), anyString());
        when(server.getRoomOperations(anyString())).thenReturn(mock(BroadcastOperations.class));

    }

    @Test
    void Create_game_test() {
        CreateGameDTO data = CreateGameDTO.builder()
                .quizId(1)
                .jwt(jwt)
                .build();
        websocketService.onCreateGame(client, data, ackRequest);
        assertEquals(1, reachedGameCreated.get());
    }

    @Test
    void Join_game_logged_in_test() {
        when(client.getSessionId()).thenReturn(uuid);
        CreateGameDTO data = CreateGameDTO.builder()
                .quizId(1)
                .jwt(jwt)
                .build();
        websocketService.onCreateGame(client, data, ackRequest);
        assertEquals(1, reachedGameCreated.get());
        JoinGameDTO gameData = JoinGameDTO.builder()
                .code(code.get())
                .jwt(jwt)
                .build();
        websocketService.onJoinGame(client, gameData, ackRequest);
        assertEquals(1, reachedGameJoined.get());
    }

    @Test
    void Player_disconnect_test() {
        Join_game_logged_in_test();
        when(client.getAllRooms()).thenReturn(Set.of(code.get()));
        websocketService.onDisconnect(client);
    }

}
