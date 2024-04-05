package edu.ntnu.idatt2105.unit.game;

import edu.ntnu.idatt2105.backend.service.JWTTokenGenerationService;
import edu.ntnu.idatt2105.backend.service.JWTTokenService;
import edu.ntnu.idatt2105.backend.util.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
