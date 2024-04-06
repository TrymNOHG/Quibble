package edu.ntnu.idatt2105.backend.unit.service;

import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.security.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class AuthenticationServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;


    @BeforeEach
    void setUp() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("test@test.test", null, Collections.emptyList());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        userRepository.save(
                User.builder()
                        .email("test@test.test")
                        .password("password")
                        .username("test")
                        .build()
        );
    }

    @Test
    void Get_logged_in_user_email_test() {
        assertEquals("test@test.test", authenticationService.getLoggedInUserEmail());
    }

    @Test
    void Get_logged_in_user_id_test() {
        assertEquals(1L, authenticationService.getLoggedInUserId());
    }

    @Test
    void Verify_user_email() {
        authenticationService.verifyUserEmail("test@test.test");
    }

    @Test
    void Verify_user_email_not_found() {
        assertThrows(ResponseStatusException.class, () -> authenticationService.verifyUserEmail("dumb mail"));
    }

    @Test
    void Get_logged_in_user_test() {
        assertEquals("test", authenticationService.getLoggedInUser().getUsername());
    }
}
