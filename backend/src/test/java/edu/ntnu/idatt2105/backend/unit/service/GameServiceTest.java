package edu.ntnu.idatt2105.backend.unit.service;

import edu.ntnu.idatt2105.backend.dto.websocket.SendAlternativesDTO;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.JWTTokenGenerationService;
import edu.ntnu.idatt2105.backend.service.JWTTokenService;
import edu.ntnu.idatt2105.backend.service.websocket.GameService;
import edu.ntnu.idatt2105.backend.util.Game;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTTokenGenerationService jwtTokenGenerationService;

    private User user;

    private void addUserToDatabase() {
        // Setup logic here
        user = User.builder()
                .username("username")
                .email("email@email.email")
                .password("password")
                .build();
        userRepository.save(user);
    }

    String generateGame() {
        addUserToDatabase();
        UUID uuid = UUID.randomUUID();
        return gameService.createGame(user.getEmail(), 1, uuid);
    }

    String generateGame(UUID uuid) {
        addUserToDatabase();
        return gameService.createGame(user.getEmail(), 1, uuid);
    }

    String generateAccessToken(User user) {
        return jwtTokenGenerationService.generateToken(user.getEmail());
    }

    @Test
    void Generate_random_code_and_create_game_test() {
        addUserToDatabase();
        UUID uuid = UUID.randomUUID();

        String code = gameService.createGame(user.getEmail(), 1, uuid);
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
    void Get_alternatives_test() {
        String code = generateGame();
        gameService.getGame(code).startGame();
//        SendAlternativesDTO alternatives = gameService.getAlternatives(code);
//        assertNotNull(alternatives);
//        assertNotNull(alternatives.questionType());
//        assertNotNull(alternatives.alternatives());
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