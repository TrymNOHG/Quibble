package edu.ntnu.idatt2105.backend.unit.service;


import com.corundumstudio.socketio.*;
import edu.ntnu.idatt2105.backend.dto.websocket.CreateGameDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.JoinGameDTO;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.JWTTokenGenerationService;
import edu.ntnu.idatt2105.backend.service.JWTTokenService;
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
    }

    @Test
    void Create_game_test() {
        AtomicInteger reachedGameCreated = new AtomicInteger();
        AtomicReference<String> code = new AtomicReference<>();
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
                return null;
            }
            return null;
        }).when(client).sendEvent(anyString(), anyString());

        CreateGameDTO data = CreateGameDTO.builder()
                .quizId(1)
                .jwt(jwt)
                .build();
        websocketService.onCreateGame(client, data, ackRequest);

        assertEquals(1, reachedGameCreated.get());
    }

//    @Test
//    void Join_game_logged_in_test() {
//        AtomicInteger reachedGameCreated = new AtomicInteger();
//        AtomicInteger reachedPlayerJoined = new AtomicInteger();
//        AtomicReference<String> code = new AtomicReference<>();
//        doAnswer(invocation -> {
//            Object arg0 = invocation.getArgument(0);
//            Object arg1 = invocation.getArgument(1);
//            if ("invalidToken".equals(arg0) && "Invalid token".equals(arg1)) {
//                // Fail the test if this method is called with these arguments
//                fail("sendEvent was called with invalidToken and Invalid token");
//            }
//            if("gameCreated".equals(arg0) && arg1 instanceof String && ((String) arg1).length() == 4) {
//                reachedGameCreated.getAndIncrement();
//                code.set((String) arg1);
//                return null;
//            }
//            if("playerJoined".equals(arg0) && arg1 instanceof String) {
//                log.info("Player joined: " + arg1);
//                reachedPlayerJoined.getAndIncrement();
//                return null;
//            }
//            return null;
//        }).when(client).sendEvent(anyString(), anyString());
//        when(server.getRoomOperations(anyString())).thenReturn(mock(BroadcastOperations.class));
////        doAnswer(invocation -> {
////            Object arg0 = invocation.getArgument(0);
////            if (!arg0.equals(code.get())) {
////                fail("getRoomOperations was called with wrong code: " + arg0);
////            }
////            return client;
////        })
//        CreateGameDTO data = CreateGameDTO.builder()
//                .quizId(1)
//                .jwt(jwt)
//                .build();
//        websocketService.onCreateGame(client, data, ackRequest);
//        assertEquals(1, reachedGameCreated.get());
//
//        when(client.getSessionId()).thenReturn(uuid);
//        when(client.getAllRooms()).thenReturn(null);
//        JoinGameDTO gameData = JoinGameDTO.builder()
//                .code(code.get())
//                .jwt(jwt)
//                .build();
//        websocketService.onJoinGame(client, gameData, ackRequest);
//        assertEquals(1, reachedPlayerJoined.get());
//    }

//    @Test
//    void Player_disconnect_test() {
//        //doReturn(true).when(client).sendEvent("playerDisconnected", anyString());
//
//        //gameService.createGame(user.)
//        when(client.getSessionId()).thenReturn(uuid);
//        when(client.getAllRooms()).thenReturn(null);
//        websocketService.onDisconnect(client);
//    }

}
