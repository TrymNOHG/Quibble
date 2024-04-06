package edu.ntnu.idatt2105.unit.game;

import edu.ntnu.idatt2105.backend.service.security.JWTTokenGenerationService;
import edu.ntnu.idatt2105.backend.service.security.JWTTokenService;
import edu.ntnu.idatt2105.backend.util.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class GameTest {

    @Mock
    private JWTTokenService jwtTokenService;

    @Mock
    private JWTTokenGenerationService jwtTokenGenerationService;

    private final String testEmail = "email.email@email.com";
    private final String testUsername = "username";
    @Nested
    class PlayerTest {
        private Game game;

        @BeforeEach
        public void setUp() {
            String code = "code";
            game = new Game(code, null, null, null, null);
        }

        @Test
        void Add_logged_in_player_test() {
        }

        @Test
        void Add_anonymous_player_test() {
            UUID uuid = UUID.randomUUID();
            game.addPlayer(uuid, "anonymous", "default-1");
            assertEquals("anonymous", game.getAnonymousPlayers().get(uuid).getUsername());
        }

        @Test
        void Add_logged_in_player_already_in_game_test() {
        }

        @Test
        void Add_anonymous_player_already_in_game_test() {

        }
    }

}
