package edu.ntnu.idatt2105.backend.unit.service;

import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserUpdateDTO;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import org.junit.Before;
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

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {

        userRepository.save(
                User.builder()
                        .email("test@test.test")
                        .password("password")
                        .username("test")
                        .build()
        );
    }

    @Test
    void Get_user_load_dto_by_email_test() {
        UserLoadDTO dto = userService.getUserLoadDTOByEmail("test@test.test");
        assertEquals("test@test.test", dto.email());
        assertEquals("test", dto.username());
        assertEquals(1L, dto.userId());
    }

    @Test
    void Get_users_by_username_fuzzy_test() {
        assertEquals(1, userService.getUsersByUsernameFuzzy("test").users().size());
        userRepository.save(
                User.builder()
                        .email("test2@test")
                        .password("password")
                        .username("tEsT2")
                        .build()
        );
        assertEquals(2, userService.getUsersByUsernameFuzzy("test").users().size());
    }

    @Test
    void Update_user_test() throws IOException {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("test@test.test", null, Collections.emptyList());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        UserUpdateDTO updateDTO = UserUpdateDTO.builder()
                .userId(1L)
                .username("new username")
                .showActivity(true)
                .showFeedback(true)
                .build();
        userService.updateUser(updateDTO);
        UserLoadDTO dto = userService.getUserLoadDTOByEmail("test@test.test");
        assertEquals("new username", dto.username());
        assertTrue(dto.showActivity());
        assertTrue(dto.showFeedback());
    }

}
