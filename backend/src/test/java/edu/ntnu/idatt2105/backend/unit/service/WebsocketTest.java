package edu.ntnu.idatt2105.backend.unit.service;


import com.corundumstudio.socketio.*;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.CreateGameDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.GameValidationDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.JoinGameDTO;
import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.MultipleChoiceRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.quiz.QuizHistoryService;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import edu.ntnu.idatt2105.backend.service.security.JWTTokenGenerationService;
import edu.ntnu.idatt2105.backend.service.security.JWTTokenService;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import edu.ntnu.idatt2105.backend.service.quiz.QuestionService;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import edu.ntnu.idatt2105.backend.service.websocket.GameService;
import edu.ntnu.idatt2105.backend.service.websocket.SocketService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
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
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizHistoryService quizHistoryService;

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
    private final AtomicInteger reachedGetQuestion = new AtomicInteger();



    // This took way to long haha
    @BeforeEach
    @Transactional
    public void setUp() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("test@test.test", null, Collections.emptyList());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        // Set up websocket service with mocked websocket server
        websocketService = new SocketService(
                server, gameService, jwtTokenService, userService, questionService, env, imageService, quizHistoryService
        );

        user = User.builder()
                .username("test")
                .email("test@test.test")
                .password("password")
                .build();
        userRepository.save(user);

        jwt = jwtTokenGenerationService.generateToken(user.getEmail());

        uuid = UUID.randomUUID();

        quizService.createQuiz("test quiz", "test@test.test");
        questionService.addQuestion(QuestionCreateDTO.builder()
                .question("test question")
                .answer("test answer")
                .type(QuestionType.MULTIPLE_CHOICE)
                .quizId(1L)
                .build(), "test@test.test");
        questionService.addQuestion(QuestionCreateDTO.builder()
                .question("test question")
                .answer("test answer")
                .type(QuestionType.MULTIPLE_CHOICE)
                .quizId(1L)
                .choices(Collections.singleton(MultipleChoiceCreateDTO.builder()
                        .alternative("choice")
                        .isCorrect(true)
                        .build()))
                .build(), "test@test.test");

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
            if("getQuestion".equals(arg0)) {
                log.info("Game started: " + arg1);
                reachedGetQuestion.getAndIncrement();
            }
            return null;
        }).when(client).sendEvent(anyString(), any());
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

    @Test
    void Start_game_test() {
        CreateGameDTO data = CreateGameDTO.builder()
                .quizId(1L)
                .jwt(jwt)
                .build();
        websocketService.onCreateGame(client, data, ackRequest);


        when(client.getAllRooms()).thenReturn(Set.of(code.get()));
        GameValidationDTO data2 = GameValidationDTO.builder()
                .jwt(jwt)
                .build();
        websocketService.onStartGame(client, data2, ackRequest);
        assertEquals(1, reachedGetQuestion.get());
    }

}
