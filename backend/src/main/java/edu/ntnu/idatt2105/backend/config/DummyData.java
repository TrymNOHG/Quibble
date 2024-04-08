package edu.ntnu.idatt2105.backend.config;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserRegisterDTO;
import edu.ntnu.idatt2105.backend.model.category.Category;
import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.category.CategoryRepository;
import edu.ntnu.idatt2105.backend.repo.category.QuizCategoryRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizAuthorRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.MultipleChoiceRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.quiz.QuestionService;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import edu.ntnu.idatt2105.backend.service.security.AuthenticationService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Logger;


/**
 * This class is responsible for inserting dummy data into the database on startup. If the data is already there, don't
 * insert it.
 *
 * @author Brage Halvorsen Kvamme, Trym Hamer Gudvangen
 * @version 1.2 07.04.2024
 */
@RequiredArgsConstructor
@Component
public class DummyData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuizRepository quizRepository;
    private final QuizAuthorRepository quizAuthorRepository;
    private final QuestionRepository questionRepository;
    private final MultipleChoiceRepository multipleChoiceRepository;
    private final Environment env;
    private final Logger logger = Logger.getLogger(DummyData.class.getName());
    private final CategoryRepository categoryRepository;
    private final QuizCategoryRepository quizCategoryRepository;
    private final QuizService quizService;
    private final AuthenticationService authenticationService;
    private final QuestionService questionService;

    /**
     * This method is responsible for inserting dummy data into the database on startup. If the data is already there,
     * don't insert it.
     *
     * @param args The command line arguments
     */
    @Transactional
    @Override
    @Profile("!test")
    public void run(String... args) throws IOException {
        for (String profile : env.getActiveProfiles()) {
            if (profile.equals("test"))
                return;
        }
        if (userRepository.findByUsername("TestUser1").isEmpty()) authenticationService.registerUser(UserRegisterDTO.builder().password("password").email("test1@test.test").username("TestUser1").build(), new HttpServletResponse() {@Override public void addCookie(Cookie cookie) {}@Override public boolean containsHeader(String name) {return false;}@Override public String encodeURL(String url) {return null;}@Override public String encodeRedirectURL(String url) {return null;}@Override public void sendError(int sc, String msg) throws IOException {}@Override public void sendError(int sc) throws IOException {}@Override public void sendRedirect(String location) throws IOException {}@Override public void setDateHeader(String name, long date) {}@Override public void addDateHeader(String name, long date) {}@Override public void setHeader(String name, String value) {}@Override public void addHeader(String name, String value) {}@Override public void setIntHeader(String name, int value) {}@Override public void addIntHeader(String name, int value) {}@Override public void setStatus(int sc) {}@Override public int getStatus() {return 0;}@Override public String getHeader(String name) {return null;}@Override public Collection<String> getHeaders(String name) {return null;}@Override public Collection<String> getHeaderNames() {return null;}@Override public String getCharacterEncoding() {return null;}@Override public String getContentType() {return null;}@Override public ServletOutputStream getOutputStream() throws IOException {return null;}@Override public PrintWriter getWriter() throws IOException {return null;}@Override public void setCharacterEncoding(String charset) {}@Override public void setContentLength(int len) {}@Override public void setContentLengthLong(long length) {}@Override public void setContentType(String type) {}@Override public void setBufferSize(int size) {}@Override public int getBufferSize() {return 0;}@Override public void flushBuffer() throws IOException {}@Override public void resetBuffer() {}@Override public boolean isCommitted() {return false;}@Override public void reset() {}@Override public void setLocale(Locale loc) {}@Override public Locale getLocale() {return null;}}, null);
        if (userRepository.findByUsername("TestUser2").isEmpty()) authenticationService.registerUser(UserRegisterDTO.builder().password("password").email("test2@test.test").username("TestUser2").build(), new HttpServletResponse() {@Override public void addCookie(Cookie cookie) {}@Override public boolean containsHeader(String name) {return false;}@Override public String encodeURL(String url) {return null;}@Override public String encodeRedirectURL(String url) {return null;}@Override public void sendError(int sc, String msg) throws IOException {}@Override public void sendError(int sc) throws IOException {}@Override public void sendRedirect(String location) throws IOException {}@Override public void setDateHeader(String name, long date) {}@Override public void addDateHeader(String name, long date) {}@Override public void setHeader(String name, String value) {}@Override public void addHeader(String name, String value) {}@Override public void setIntHeader(String name, int value) {}@Override public void addIntHeader(String name, int value) {}@Override public void setStatus(int sc) {}@Override public int getStatus() {return 0;}@Override public String getHeader(String name) {return null;}@Override public Collection<String> getHeaders(String name) {return null;}@Override public Collection<String> getHeaderNames() {return null;}@Override public String getCharacterEncoding() {return null;}@Override public String getContentType() {return null;}@Override public ServletOutputStream getOutputStream() throws IOException {return null;}@Override public PrintWriter getWriter() throws IOException {return null;}@Override public void setCharacterEncoding(String charset) {}@Override public void setContentLength(int len) {}@Override public void setContentLengthLong(long length) {}@Override public void setContentType(String type) {}@Override public void setBufferSize(int size) {}@Override public int getBufferSize() {return 0;}@Override public void flushBuffer() throws IOException {}@Override public void resetBuffer() {}@Override public boolean isCommitted() {return false;}@Override public void reset() {}@Override public void setLocale(Locale loc) {}@Override public Locale getLocale() {return null;}}, null);

        User quizAdmin = userRepository.findByUsername("TestUser1").get();
        if (quizService.getAllQuizzesByUser(quizAdmin.getUserId()).quizzes().isEmpty()) {
            logger.info("Creating dummy quiz data");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test1@test.test", null, Collections.emptyList());
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            QuizLoadDTO quizDTO = quizService.createQuiz("Capitals of Scandinavia", "test1@test.test");
            quizService.updateQuiz(QuizUpdateDTO.builder().difficulty(Difficulty.MEDIUM).quizId(quizDTO.quizId()).build());
            questionService.addQuestion(QuestionCreateDTO.builder().question("What is the capital of Norway?").answer("Oslo").type(QuestionType.MULTIPLE_CHOICE).choices(Set.of(MultipleChoiceCreateDTO.builder().alternative("Oslo").isCorrect(true).build(), MultipleChoiceCreateDTO.builder().alternative("Trondheim").isCorrect(false).build(), MultipleChoiceCreateDTO.builder().alternative("Bergen").isCorrect(false).build(), MultipleChoiceCreateDTO.builder().alternative("Gjøvik").isCorrect(false).build())).quizId(quizDTO.quizId()).build(), "test1@test.test");
            questionService.addQuestion(QuestionCreateDTO.builder().question("What is the capital of Sweden?").answer("Stockholm").type(QuestionType.MULTIPLE_CHOICE).choices(Set.of(MultipleChoiceCreateDTO.builder().alternative("Stockholm").isCorrect(true).build(), MultipleChoiceCreateDTO.builder().alternative("Malmö").isCorrect(false).build(), MultipleChoiceCreateDTO.builder().alternative("Åre").isCorrect(false).build(), MultipleChoiceCreateDTO.builder().alternative("Fish").isCorrect(false).build())).quizId(quizDTO.quizId()).build(), "test1@test.test");
            SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        }

        Set<Category> categories = createListOfCategories(new String[]{"School", "Movies", "Hobbies", "Sports"});
        if (categoryRepository.findAll().isEmpty()) categoryRepository.saveAll(categories);
    }

    private Set<Category> createListOfCategories(String[] categoryNames) {
        Set<Category> categories = new HashSet<>();
        for(String name : categoryNames) categories.add(Category.builder().categoryName(name).build());
        return categories;
    }
}
