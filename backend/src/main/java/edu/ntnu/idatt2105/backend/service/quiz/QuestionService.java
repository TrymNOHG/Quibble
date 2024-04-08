package edu.ntnu.idatt2105.backend.service.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuestionDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionEditDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.AlternativeDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.SendAlternativesDTO;
import edu.ntnu.idatt2105.backend.exception.UnauthorizedException;
import edu.ntnu.idatt2105.backend.exception.notfound.NotFoundException;
import edu.ntnu.idatt2105.backend.exception.notfound.QuestionNotFoundException;
import edu.ntnu.idatt2105.backend.exception.notfound.QuizNotFoundException;
import edu.ntnu.idatt2105.backend.mapper.quiz.MultipleChoiceMapper;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuestionMapper;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizMapper;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizAuthorRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.MultipleChoiceRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import static edu.ntnu.idatt2105.backend.util.SortingUtil.sortListWithUuidSeed;

/**
 * Service class for handling questions.
 *
 * @author Brage Halvorsen Kvamme, Trym Hamer Gudvangen
 * @version 1.1 05.04.2024
 * @see Question
 */
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final MultipleChoiceRepository multipleChoiceRepository;
    private final QuizMapper quizMapper;
    Logger LOGGER = Logger.getLogger(QuestionService.class.getName());
    private final QuizAuthorRepository quizAuthorRepository;
    private final UserRepository userRepository;

    /**
     * This method retrieves a question based on id for web sockets multiplayer.
     * @param questionId        The id of the question.
     * @param hostUUID          The UUID of the host.
     * @return                  The question as a DTO.
     */
    @Transactional
    public QuestionDTO getQuestionDTO(long questionId, UUID hostUUID) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        return QuestionDTO.builder()
                .id(question.getQuestionId())
                .question(question.getQuestion())
                .answer(getCorrectAnswer(questionId))
                .questionType(question.getQuestionType().name())
                .options(sortListWithUuidSeed(
                        question.getChoices()
                                .stream()
                                .map(MultipleChoiceMapper.INSTANCE::multipleChoiceToMultipleChoiceDTO)
                                .toList(),
                        hostUUID,
                        MultipleChoiceDTO::alternative
                ))
                .build();
    }

    /**
     * This method retrieves the different alternatives for a question given the question id.
     * @param questionId    The id of the question.
     * @param hostUUID      The host UUID.
     * @return              The alternative as a DTO.
     */
    @Transactional
    public SendAlternativesDTO getAlternatives(long questionId, UUID hostUUID) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        return SendAlternativesDTO.builder()
                .questionType(question.getQuestionType().name())
                .options(sortListWithUuidSeed(
                        question.getChoices()
                                .stream()
                                .map(choice -> AlternativeDTO.builder().alternative(choice.getAlternative()).build())
                                .toList(),
                        hostUUID,
                        AlternativeDTO::alternative
                ))
                .build();
    }

    /**
     * THis method retrieves the correct answer given a question id.
     * @param questionId    The question id.
     * @return              The correct answer.
     */
    @Transactional
    public String getCorrectAnswer(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        if (question.getAnswer() == null || question.getAnswer().isBlank()) {
            return Objects.requireNonNull(question.getChoices().stream().filter(MultipleChoice::isCorrect).findFirst().orElse(null))
            .getAlternative();
        }
        LOGGER.info("Question: " + question.getQuestion() + " Answer: " + question.getAnswer());
        return question.getAnswer();
    }

    /**
     * This method adds a new question to the database.
     *
     * @param questionCreateDTO The information of the question.
     * @return The new quiz.
     */
    @Transactional
    public QuizLoadDTO addQuestion(QuestionCreateDTO questionCreateDTO, String email) {
        authorizeOwnerOrCollaborator(questionCreateDTO.quizId(), email);

        LOGGER.info("Attempting to retrieve quiz");
        Quiz quiz = quizRepository.findById(
                questionCreateDTO.quizId()
                )
                .orElseThrow(
                        () -> new QuizNotFoundException(questionCreateDTO.quizId().toString()
                        )
                );
        LOGGER.info("Quiz found.");
        LOGGER.info("Creating Question object.");
        Question question = QuestionMapper.INSTANCE.questionCreateDTOToQuestion(questionCreateDTO);
        quiz.addQuestion(question);

        LOGGER.info("Saving question to database.");
        Question savedQuestion = questionRepository.save(question);
        quizRepository.save(quiz);
        LOGGER.info("Question saved to database!");

        if (questionCreateDTO.choices() != null) {
            LOGGER.info("Adding multiple choices.");
            for (MultipleChoiceCreateDTO choice : questionCreateDTO.choices()) {
                addMultipleChoiceAlternative(choice, savedQuestion.getQuestionId());
            }
        }

        LOGGER.info("Retrieving newest quiz.");
        return quizMapper.quizToQuizLoadDTO(
                quizRepository.findById(questionCreateDTO.quizId())
                .orElseThrow(
                        () -> new QuizNotFoundException(questionCreateDTO.quizId().toString())
                )
        );
    }

    /**
     * This method handles the logic of editing a question in the database.
     * @param questionEditDTO   The new information to edit.
     * @return                  The quiz as a QuizLoadDTO.
     */
    @Transactional
    public QuizLoadDTO editQuestion(QuestionEditDTO questionEditDTO, String email) {
        authorizeOwnerOrCollaborator(questionEditDTO.quizId(), email);

        LOGGER.info("Attempting to retrieve quiz");
        Quiz quiz = quizRepository.findById(questionEditDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException(questionEditDTO.quizId().toString()));
        LOGGER.info("Quiz found.");
        LOGGER.info("Retrieving Question object.");
        Question question = questionRepository.findById(questionEditDTO.questionId())
                .orElseThrow(
                        () -> new QuestionNotFoundException(questionEditDTO.quizId().toString())
                );

        LOGGER.info("Editing Question.");

        if (questionEditDTO.question() != null) {
            question.setQuestion(questionEditDTO.question());
        }

        if (questionEditDTO.answer() != null) {
            question.setAnswer(questionEditDTO.answer());
        }

        if (questionEditDTO.type() != null) {
            question.setQuestionType(questionEditDTO.type());
            if(questionEditDTO.type() != QuestionType.MULTIPLE_CHOICE) {
                deleteChoices(questionEditDTO.questionId());
            }
        }

        LOGGER.info("Saving question to database.");
        Question savedQuestion = questionRepository.save(question);
        LOGGER.info("Question saved to database!");

        if (questionEditDTO.choices() != null) {
            LOGGER.info("Editing multiple choices.");
            for (MultipleChoiceDTO choice : questionEditDTO.choices()) {
                if(choice.multipleChoiceId() == null) {
                    MultipleChoiceCreateDTO multipleChoiceCreateDTO = MultipleChoiceCreateDTO
                            .builder()
                            .isCorrect(choice.isCorrect())
                            .alternative(choice.alternative())
                            .build();
                    addMultipleChoiceAlternative(multipleChoiceCreateDTO, question.getQuestionId());
                } else {
                    editMultipleChoiceAlternative(choice);
                }
            }
            LOGGER.info("Choices edited.");
        }

        LOGGER.info("Retrieving newest quiz.");
        return quizMapper.quizToQuizLoadDTO(quizRepository.findById(questionEditDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException(questionEditDTO.quizId().toString())));

    }

    /**
     * This method deletes a question from the database.
     * @param questionId    The question's id.
     */
    public void deleteQuestion(Long questionId, String email) {
        Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new QuestionNotFoundException("Question with id: " + questionId));
        authorizeOwnerOrCollaborator(question.getQuiz().getQuizId(), email);
        LOGGER.info("Deleting question with id: " + questionId);
        questionRepository.delete(question);
    }

    /**
     * This method adds a new multiple choice alternative to a question.
     *
     * @param multipleChoiceCreateDTO The information surrounding the choice.
     * @param questionId              The question to be added under.
     */
    private void addMultipleChoiceAlternative(MultipleChoiceCreateDTO multipleChoiceCreateDTO, Long questionId) {
        LOGGER.info("Finding question to add alternative to.");
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question"));

        MultipleChoice multipleChoice = MultipleChoiceMapper.INSTANCE.multipleChoiceCreateDTOToMultipleChoice(multipleChoiceCreateDTO);
        multipleChoice.setQuestion(question);

        multipleChoiceRepository.save(multipleChoice);
        LOGGER.info("Multiple choice saved.");
    }

    /**
     * This method edits a multiple choice alternative.
     *
     * @param multipleChoiceDTO The new information for the multiple choice alternative.
     */
    private void editMultipleChoiceAlternative(MultipleChoiceDTO multipleChoiceDTO) {
        LOGGER.info("Editing multiple choice alternative.");
        if (multipleChoiceDTO.multipleChoiceId() == null){
            throw new NullPointerException("Multiple choice id should not be null.");
        }
        MultipleChoice multipleChoice = multipleChoiceRepository.findById(multipleChoiceDTO.multipleChoiceId())
                .orElseThrow(() -> new QuestionNotFoundException("Multiple choice : "
                        + multipleChoiceDTO.multipleChoiceId()));
        multipleChoice.setAlternative(multipleChoiceDTO.alternative());
        multipleChoice.setCorrect(multipleChoiceDTO.isCorrect());

        multipleChoiceRepository.save(multipleChoice);
        LOGGER.info("Edits have been saved.");
    }

    /**
     * This method authorizes a user with owner or collaborator privileges.
     * @param quizId    The id of the quiz.
     * @param email     The email of the user.
     */
    private void authorizeOwnerOrCollaborator(Long quizId, String email) {
        LOGGER.info("Checking authorization of " + email);
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email)).getUserId();

        if (!isOwnerOrCollaborator(quizId, userId)) {
            throw new UnauthorizedException(email);
        }
        LOGGER.info("User is authorized.");
    }

    /**
     * This method deletes the choices associated with a given question.
     * @param questionId    The id of the question.
     */
    private void deleteChoices(Long questionId) {
        LOGGER.info("Finding choices for a given question.");
        Set<MultipleChoice> quizChoices = multipleChoiceRepository.findMultipleChoicesByQuestionQuestionId(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId.toString()));
        LOGGER.info("Deleting all of the choices.");
        multipleChoiceRepository.deleteAll(quizChoices);
    }

    /**
     * This method checks whether a user is the owner/admin of a quiz.
     * @param quizId    The id of the quiz.
     * @param userId    The id of the user.
     * @return          Status of whether is owner.
     */
    private boolean isOwner(Long quizId, Long userId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId.toString()));
        return quiz.getAdmin().getUserId().equals(userId);
    }

    /**
     * This method checks whether a user is a collaborator of a quiz.
     * @param quizId    The id of the quiz.
     * @param userId    The id of the user.
     * @return          Status of whether is collaborator.
     */
    private boolean isCollaborator(Long quizId, Long userId) {
        return quizAuthorRepository.findQuizAuthorByQuizQuizIdAndUserUserId(quizId, userId)
                .isPresent();
    }

    /**
     * This method checks whether is a user is either an owner or collaborator of a quiz.
     * @param quizId    The id of the quiz.
     * @param userId    The id of the user.
     * @return          Status whether user is owner or collaborator.
     */
    private boolean isOwnerOrCollaborator(Long quizId, Long userId) {
        return isOwner(quizId, userId) || isCollaborator(quizId, userId);
    }


}

