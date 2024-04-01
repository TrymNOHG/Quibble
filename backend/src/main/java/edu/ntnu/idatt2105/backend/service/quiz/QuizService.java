package edu.ntnu.idatt2105.backend.service.quiz;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.exception.notfound.NotFoundException;
import edu.ntnu.idatt2105.backend.mapper.quiz.MultipleChoiceMapper;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuestionMapper;
import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.exception.notfound.QuizNotFoundException;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizMapper;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.MultipleChoiceRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * This service provides the logic for the Quiz entity.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 01.04.2024
 */
@Service
@RequiredArgsConstructor
public class QuizService {

    private final Logger LOGGER = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuizMapper quizMapper;
    private final QuestionMapper questionMapper;
    private final MultipleChoiceRepository multipleChoiceRepository;

    public QuizLoadDTO updateQuiz(QuizUpdateDTO quizUpdateDTO) {
        //TODO: check that user trying to change is owner or collaborator
        LOGGER.info("Attempting to retrieve quiz with id: " + quizUpdateDTO.quizId());
        Quiz quiz = quizRepository.findById(quizUpdateDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException("Id: " + quizUpdateDTO.quizId()));

        if (quizUpdateDTO.newName() != null) {
            LOGGER.info("Updating quiz name");
            quiz.setQuizName(quizUpdateDTO.newName());
        }

        if (quizUpdateDTO.newDescription() != null) {
            LOGGER.info("Updating quiz description");
            quiz.setQuizDescription(quizUpdateDTO.newDescription());
        }

        if (quizUpdateDTO.difficulty() != null) {
            LOGGER.info("Updating quiz difficulty");
            quiz.setDifficulty(quizUpdateDTO.difficulty());
        }

        Quiz savedQuiz = quizRepository.save(quiz);

        return quizMapper.quizToQuizLoadDTO(savedQuiz);
    }

    public Quiz getQuizById(long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz with id " + quizId + " not found"));
    }

    @Transactional
    public QuizLoadDTO createQuiz(String quizName, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + adminEmail + " not found"));
        Quiz quiz = Quiz.builder()
                .quizName(quizName)
                .admin(admin)
                .difficulty(Difficulty.EASY)
                .build();
        quiz = quizRepository.save(quiz);
        return QuizLoadDTO.builder()
                .quizId(quiz.getQuizId())
                .quizName(quiz.getQuizName())
                .admin_id(admin.getUserId())
                .difficulty(quiz.getDifficulty())
                .build();
    }

    /**
     * This method adds a new question to the database.
     * @param questionCreateDTO     The information of the question.
     * @return                      The new quiz.
     */
    public QuizLoadDTO addQuestion(QuestionCreateDTO questionCreateDTO) {
        //TODO: check that user is editing question they are authorized to.
        // Might be a little unnecessary to send the whole quiz again...
        //Check if Quiz exist
        LOGGER.info("Attempting to retrieve quiz");
        Quiz quiz = quizRepository.findById(questionCreateDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException(questionCreateDTO.quizId().toString()));
        LOGGER.info("Quiz found.");
        LOGGER.info("Creating Question object.");
        Question question = questionMapper.questionCreateDTOToQuestion(questionCreateDTO);
        question.setQuiz(quiz);

        LOGGER.info("Saving question to database.");
        Question savedQuestion = questionRepository.save(question);
        LOGGER.info("Question saved to database!");

        if(questionCreateDTO.choices() != null) {
            LOGGER.info("Adding multiple choices.");
            for (MultipleChoiceDTO choice : questionCreateDTO.choices()) {
                addMultipleChoiceAlternative(choice, savedQuestion.getQuestionId());
            }
        }

        LOGGER.info("Retrieving newest quiz.");
        return quizMapper.quizToQuizLoadDTO(quizRepository.findById(questionCreateDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException(questionCreateDTO.quizId().toString())));
    }

    /**
     * This method adds a new multiple choice alternative to a question.
     * @param multipleChoiceDTO     The information surrounding the choice.
     * @param questionId            The question to be added under.
     */
    private void addMultipleChoiceAlternative(MultipleChoiceDTO multipleChoiceDTO, Long questionId) {
        // Check if question exists
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question"));

        MultipleChoice multipleChoice = MultipleChoiceMapper.INSTANCE.multipleChoiceDTOToMultipleChoice(multipleChoiceDTO);
        multipleChoice.setQuestion(question);
        multipleChoiceRepository.save(multipleChoice);
    }

}
