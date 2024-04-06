package edu.ntnu.idatt2105.backend.unit.service;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizFilterDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class QuizServiceTest {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizService quizService;


    @BeforeEach
    public void setUp() {
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
    @Transactional
    void Create_quiz_test() {
        quizService.createQuiz("Test quiz", "test@test.test");
        quizRepository.findByQuizName("Test quiz").ifPresentOrElse(
                quiz -> {
                    assertEquals("Test quiz", quiz.getQuizName());
                    assertEquals("test@test.test", quiz.getAdmin().getEmail());
                },
                () -> fail("Quiz not found")
        );
    }

    @Test
    void Get_quiz_by_id_test() {
        quizService.createQuiz("Test quiz", "test@test.test");
        Quiz quiz = quizService.getQuizById(1L);
        assertEquals("Test quiz", quiz.getQuizName());
        assertEquals("test@test.test", quiz.getAdmin().getEmail());
    }

    @Test
    void Delete_quiz_test() {
        quizService.createQuiz("Test quiz", "test@test.test");
        quizService.deleteQuiz(1L);
        assertTrue(quizRepository.findByQuizName("Test quiz").isEmpty());
    }

    @Test
    @Transactional
    void Load_all_quizzes_test() {
        quizService.createQuiz("Test quiz", "test@test.test");
        assertEquals(1, quizService.loadAllQuiz().quizzes().size());
        quizService.createQuiz("Test quiz2", "test@test.test");
        assertEquals(2, quizService.loadAllQuiz().quizzes().size());
    }

    @Test
    @Transactional
    void Get_filtered_quizzes() {
        quizService.createQuiz("Test quiz1", "test@test.test");
        quizService.createQuiz("Test quiz3", "test@test.test");
        quizService.createQuiz("Test quiz13", "test@test.test");
        quizService.createQuiz("Test quiz14", "test@test.test");

        assertEquals(3, quizService.getFilteredQuizzes(QuizFilterDTO.builder()
                .pageSize(3)
                .pageNumber(0)
                .build()).getSize());

        quizService.getFilteredQuizzes(QuizFilterDTO.builder()
                .pageSize(10)
                .pageNumber(0)
                .build()).forEach(quiz -> assertTrue(quiz.quizName().contains("Test quiz"))
        );

        assertEquals(3, quizService.getFilteredQuizzes(QuizFilterDTO.builder()
                .name("Test quiz1")
                .pageSize(10)
                .pageNumber(0)
                .build()).get().count());

        assertEquals(3, quizService.getFilteredQuizzes(QuizFilterDTO.builder()
                .name("Test quiz1")
                        .keywords(Collections.emptySet())
                        .categories(Collections.emptySet())
                        .minStars(0L)
                        .difficulties(Collections.emptySet())
                .pageSize(10)
                .pageNumber(0)
                .build()).get().count());


    }

    @Test
    void Get_quiz_by_id_not_found_test() {
        assertThrows(IllegalArgumentException.class, () -> quizService.getQuizById(1L));
    }

    @Test
    void Update_quiz_test() {
        quizService.createQuiz("Test quiz", "test@test.test");
        quizService.updateQuiz(QuizUpdateDTO.builder()
                .newName("New name")
                .quizId(1L)
                .newDescription("New description")
                .difficulty(Difficulty.MEDIUM)
                .build());
        Quiz quiz = quizService.getQuizById(1L);
        assertEquals("New name", quiz.getQuizName());
        assertEquals("New description", quiz.getQuizDescription());
        assertEquals(Difficulty.MEDIUM, quiz.getDifficulty());
    }

    @Test
    void Add_collaborator_test() {
        quizService.createQuiz("Test quiz", "test@test.test");
        assertThrows(Exception.class, () -> quizService.addCollaborator(QuizAuthorDTO.builder()
                .quizId(1L)
                .userId(2L)
                .build()));
    }

    @Test
    @Transactional
    void Add_collaborator_test2() {
        quizService.createQuiz("Test quiz", "test@test.test");
        userRepository.save(
                User.builder()
                        .email("collab@collab")
                        .password("password")
                        .username("collab")
                        .build()
        );
        quizService.addCollaborator(QuizAuthorDTO.builder()
                .quizId(1L)
                .userId(2L)
                .build());
        Quiz quiz = quizService.getQuizById(1L);
        assertEquals(1, quiz.getCollaborators().size());
        assertEquals("collab@collab", quiz.getCollaborators().stream().findFirst().get().getUser().getEmail());
    }


}
