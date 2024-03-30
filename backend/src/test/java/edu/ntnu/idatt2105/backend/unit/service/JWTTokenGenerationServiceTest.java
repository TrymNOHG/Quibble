package edu.ntnu.idatt2105.backend.unit.service;

import edu.ntnu.idatt2105.backend.config.UserConfig;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.JWTTokenGenerationService;
import edu.ntnu.idatt2105.backend.service.JWTTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class JWTTokenGenerationServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTTokenService jwtTokenService;

    @Autowired
    private JWTTokenGenerationService jwtTokenGenerationService;

    private String email = "email";
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        // Setup logic here
        User user = User.builder()
                .profilePicLink("profilePicLink")
                .username("username")
                .email(email)
                .password("password")
                .build();
        userRepository.save(user);

        userDetails = new UserConfig(user);
    }
    @Test
    void Generate_token_test() {
        String token = jwtTokenGenerationService.generateToken(email);
        assertNotNull(token, "Token should not be null");
        jwtTokenService.isValidToken(jwtTokenService.getJwt(token));
    }

    @Test
    void Generate_token_test_with_user_details() {
        String token = jwtTokenGenerationService.generateToken(email);
        assertNotNull(token, "Token should not be null");
        jwtTokenService.isValidToken(jwtTokenService.getJwt(token), userDetails);
    }

    @Test
    void Generate_refresh_token_test() {
        String token = jwtTokenGenerationService.generateRefreshToken(email);
        assertNotNull(token, "Token should not be null");
        jwtTokenService.isValidToken(jwtTokenService.getJwt(token));
    }

    @Test
    void Generate_refresh_token_test_with_user_details() {
        String token = jwtTokenGenerationService.generateRefreshToken(email);
        assertNotNull(token, "Token should not be null");
        jwtTokenService.isValidToken(jwtTokenService.getJwt(token), userDetails);
    }
}
