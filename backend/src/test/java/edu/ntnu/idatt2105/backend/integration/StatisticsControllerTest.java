package edu.ntnu.idatt2105.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2105.backend.dto.quiz.history.QuizHistoryLoadAllDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.quiz.QuizHistoryService;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import edu.ntnu.idatt2105.backend.service.security.JWTTokenGenerationService;
import edu.ntnu.idatt2105.backend.util.Player;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JWTTokenGenerationService jwtTokenGenerationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizHistoryService quizHistoryService;

    private String jwt;

    @Transactional
    @BeforeEach
    void setUp() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("test@test.test", null, Collections.emptyList());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = User.builder()
                .email("test@test.test")
                .password("password")
                .username("test")
                .build();
        userRepository.save(user);
        quizService.createQuiz("test quiz", "test@test.test");
        Quiz quiz = quizService.getQuizById(1L);
        List<Player> player = List.of(new Player(user, UUID.randomUUID()));
        quizHistoryService.addHistory(player, quiz);
        jwt = jwtTokenGenerationService.generateToken("test@test.test");
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
    }

    @Test
    void Get_statistics_for_user_test() throws Exception {
        String histories = mockMvc.perform(get("/api/v1/private/stat/user")
                .header(HttpHeaders.AUTHORIZATION,
                        "Bearer "+jwt))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        QuizHistoryLoadAllDTO allDTO = objectMapper.readValue(histories, QuizHistoryLoadAllDTO.class);
        assertEquals(1, allDTO.allHistoricalEvents().size());
    }
}
