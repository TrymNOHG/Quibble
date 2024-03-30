package edu.ntnu.idatt2105.backend.unit.service;

import edu.ntnu.idatt2105.backend.config.UserConfig;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.JWTTokenGenerationService;
import edu.ntnu.idatt2105.backend.service.JWTTokenService;
import io.jsonwebtoken.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class JWTTokenServiceTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTTokenGenerationService jwtTokenGenerationService;

    @Autowired
    private JWTTokenService jwtTokenService;

    private User user;
    private String token;
    private Jwt jwt;

    @BeforeEach
    public void setUp() {
        // Setup logic here
        user = User.builder()
                .profilePicLink("profilePicLink")
                .username("username")
                .email("email")
                .password("password")
                .build();
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            userRepository.save(user);
        }
    }

    @Test
    void Get_user_email_from_token_jwt_object_test() {
        token = jwtTokenGenerationService.generateToken(user.getEmail());
        jwt = jwtTokenService.getJwt(token);
        String email = jwtTokenService.getUserEmail(jwt);
        assertEquals(email, user.getEmail());
    }

    @Test
    void Get_user_email_from_token_string_test() {
        token = jwtTokenGenerationService.generateToken(user.getEmail());
        String email = jwtTokenService.getUserEmail(token);
        assertEquals(email, user.getEmail());
    }

    @Test
    void Get_jwt_object_from_token_string_test() {
        token = jwtTokenGenerationService.generateToken(user.getEmail());
        Jwt jwt = jwtTokenService.getJwt(token);
        assertNotNull(jwt);
    }

    @Test
    void Get_user_id_from_token_jwt_object_test() {
        token = jwtTokenGenerationService.generateToken(user.getEmail());
        jwt = jwtTokenService.getJwt(token);
        long id = jwtTokenService.getUserId(jwt);
        assertEquals(id, user.getUserId());
    }

    @Test
    void Get_user_details_from_email_test() {
        assertEquals(new UserConfig(user).getUsername(), jwtTokenService.getUserDetails(user.getEmail()).getUsername());
        assertEquals(new UserConfig(user).getPassword(), jwtTokenService.getUserDetails(user.getEmail()).getPassword());
    }

    @Test
    void Get_user_details_from_email_not_found_test() {
        assertThrows(Exception.class, () -> jwtTokenService.getUserDetails("notfound"));
    }

    @Test
    void Get_user_details_from_email_null_test() {
        assertThrows(Exception.class, () -> jwtTokenService.getUserDetails(null));
    }

    @Test
    void Get_user_details_from_user_id_test() {
        UserDetails userDetails = jwtTokenService.getUserDetails(user.getUserId());
        UserDetails expected = new UserConfig(user);
        assertEquals(expected.getUsername(), userDetails.getUsername());
        assertEquals(expected.getPassword(), userDetails.getPassword());
    }

    @Test
    void Get_user_details_from_user_id_not_found_test() {
        assertThrows(IOException.class, () -> jwtTokenService.getUserDetails("notfound"));
    }

    @Test
    void Get_user_details_from_user_id_null_test() {
        assertThrows(IOException.class, () -> jwtTokenService.getUserDetails(null));
    }

    @Test
    void Is_valid_token_test() {
        token = jwtTokenGenerationService.generateToken(user.getEmail());
        assertTrue(jwtTokenService.isValidToken(jwtTokenService.getJwt(token)));
    }

    @Test
    void Is_valid_token_with_user_details_test() {
        token = jwtTokenGenerationService.generateToken(user.getEmail());
        assertTrue(jwtTokenService.isValidToken(jwtTokenService.getJwt(token), new UserConfig(user)));
    }


}
