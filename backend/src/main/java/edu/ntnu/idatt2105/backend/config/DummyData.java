package edu.ntnu.idatt2105.backend.config;

import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizAuthorRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.MultipleChoiceRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

/**
 * This class is responsible for inserting dummy data into the database on startup. If the data is already there, don't
 * insert it.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.1 29.03.2024
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

    /**
     * This method is responsible for inserting dummy data into the database on startup. If the data is already there,
     * don't insert it.
     *
     * @param args The command line arguments
     */
    @Transactional
    @Override
    public void run(String... args) {
        for (String profile : env.getActiveProfiles()) {
            if (profile.equals("test"))
                return;
        }
        User user = User.builder().username("TestUser").email("test@test.test").password(passwordEncoder.encode("password")).build();

        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            logger.info("TestUser: not found in database, adding TestUser to database");
            userRepository.save(user);
        } else {
            logger.info("TestUser: already in database");
            return;
        }

        User user2 = User.builder().username("test User2").email("test2@test.test").password(passwordEncoder.encode("password")).build();

        if (userRepository.findByUsername(user2.getUsername()).isEmpty()) {
            logger.info("TestUser: not found in database, adding TestUser to database");
            user2 = userRepository.save(user2);
        } else {
            logger.info("TestUser: already in database");
            return;
        }

        User quizAdmin = userRepository.findByUsername("TestUser").orElseThrow();
        Quiz quiz = Quiz.builder().quizName("Capitals of Scandinavia").quizDescription("A quiz about the capitals of Scandinavia").admin(quizAdmin).difficulty(Difficulty.EASY).build();

        if (quizRepository.findByQuizName(quiz.getQuizName()).isEmpty()) {// QuizName is not unique, but this is for testing purposes.
            logger.info("TestQuiz: Capitals of Scandinavia Quiz not found in database, adding quiz to database");
            quiz = quizRepository.save(quiz);
            logger.info("TestQuiz: Capitals of Scandinavia Quiz added to database");
        } else
            logger.info("TestQuiz: already in database");

        Question question1 = Question.builder().question("What is the capital of Norway?").questionType(QuestionType.MULTIPLE_CHOICE).answer("Oslo").quiz(quiz).build();
        questionRepository.save(question1);
        Set<MultipleChoice> choices = Set.of(MultipleChoice.builder().alternative("Oslo").isCorrect(true).question(question1).build(), MultipleChoice.builder().alternative("Bergen").isCorrect(false).question(question1).build(), MultipleChoice.builder().alternative("Trondheim").isCorrect(false).question(question1).build(), MultipleChoice.builder().alternative("Stavanger").isCorrect(false).question(question1).build());
        multipleChoiceRepository.saveAll(choices);
        Question question2 = Question.builder().question("What is the capital of Sweden?").answer("Stockholm").questionType(QuestionType.SHORT_ANSWER).quiz(quiz).build();
        Question question3 = Question.builder().question("What is the capital of Denmark?").answer("Copenhagen").questionType(QuestionType.SHORT_ANSWER).quiz(quiz).build();
        questionRepository.save(question2);
        questionRepository.save(question3);
        QuizAuthor quizAuthor = QuizAuthor.builder().user(user2).quiz(quiz).build();
        quizAuthorRepository.save(quizAuthor);
        logger.info("Successfully added quiz author");

    }
}
