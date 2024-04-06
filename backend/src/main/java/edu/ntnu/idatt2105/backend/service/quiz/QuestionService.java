package edu.ntnu.idatt2105.backend.service.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuestionDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionEditDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.SendAlternativesDTO;
import edu.ntnu.idatt2105.backend.exception.notfound.NotFoundException;
import edu.ntnu.idatt2105.backend.exception.notfound.QuestionNotFoundException;
import edu.ntnu.idatt2105.backend.exception.notfound.QuizNotFoundException;
import edu.ntnu.idatt2105.backend.mapper.quiz.MultipleChoiceMapper;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuestionMapper;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizMapper;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.MultipleChoiceRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Service class for handling questions.
 *
 * @version 1.1 31.05.2021
 * @author brage
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

    public boolean isCorrectAnswer(Question question, String answer) {
        return switch (question.getQuestionType()) {
            case MULTIPLE_CHOICE -> question.getChoices().stream().anyMatch(
                    choice -> choice.isCorrect() && choice.getAlternative().equalsIgnoreCase(answer)
            );
            case TRUE_FALSE, SHORT_ANSWER, LONG_ANSWER, FILL_IN_BLANK, FLASHCARD -> question.getAnswer().equalsIgnoreCase(answer);
            default -> question.getAnswer().equalsIgnoreCase(answer);
        };
    }

    @Transactional
    public QuestionDTO getQuestionDTO(long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        return QuestionDTO.builder()
                .id(question.getQuestionId())
                .question(question.getQuestion())
                .answer(getCorrectAnswer(questionId))
                .questionType(question.getQuestionType().name())
                .options(question.getChoices().stream().map(MultipleChoice::getAlternative).toList())
                .build();
    }

    @Transactional
    public SendAlternativesDTO getAlternatives(long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        return SendAlternativesDTO.builder()
                .questionType(question.getQuestionType().name())
                .alternatives(question.getChoices().stream().map(MultipleChoice::getAlternative).toArray(String[]::new))
                .build();
    }

    @Transactional
    public String getCorrectAnswer(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        if (question.getAnswer() == null || question.getAnswer().isBlank()) {
            return question.getChoices().stream().filter(MultipleChoice::isCorrect).findFirst().orElseThrow(
                    () -> new IllegalStateException("No correct answer found for question " + question.getQuestionId())
            ).getAlternative();
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
    public QuizLoadDTO addQuestion(QuestionCreateDTO questionCreateDTO) {
        //TODO: check that user is editing question they are authorized to.
        // Might be a little unnecessary to send the whole quiz again...
        //Check if Quiz exist
        LOGGER.info("Attempting to retrieve quiz");
        Quiz quiz = quizRepository.findById(questionCreateDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException(questionCreateDTO.quizId().toString()));
        LOGGER.info("Quiz found.");
        LOGGER.info("Creating Question object.");
        Question question = QuestionMapper.INSTANCE.questionCreateDTOToQuestion(questionCreateDTO);
        question.setQuiz(quiz);

        LOGGER.info("Saving question to database.");
        Question savedQuestion = questionRepository.save(question);
        LOGGER.info("Question saved to database!");

        if (questionCreateDTO.choices() != null) {
            LOGGER.info("Adding multiple choices.");
            for (MultipleChoiceCreateDTO choice : questionCreateDTO.choices()) {
                addMultipleChoiceAlternative(choice, savedQuestion.getQuestionId());
            }
        }

        LOGGER.info("Retrieving newest quiz.");
        return quizMapper.quizToQuizLoadDTO(quizRepository.findById(questionCreateDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException(questionCreateDTO.quizId().toString())));
    }

    /**
     * This method handles the logic of editing a question in the database.
     * @param questionEditDTO   The new information to edit.
     * @return                  The quiz as a QuizLoadDTO.
     */
    @Transactional
    public QuizLoadDTO editQuestion(QuestionEditDTO questionEditDTO) {
        //TODO: check that user is editing question they are authorized to.
        LOGGER.info("Attempting to retrieve quiz");
        Quiz quiz = quizRepository.findById(questionEditDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException(questionEditDTO.quizId().toString()));
        LOGGER.info("Quiz found.");
        LOGGER.info("Retrieving Question object.");
        Question question = questionRepository.findById(questionEditDTO.questionId())
                .orElseThrow(() -> new QuestionNotFoundException(questionEditDTO.quizId().toString()));

        LOGGER.info("Editing Question.");

        if (questionEditDTO.question() != null) {
            question.setQuestion(questionEditDTO.question());
        }

        if (questionEditDTO.answer() != null) {
            question.setAnswer(questionEditDTO.answer());
        }

        if (questionEditDTO.type() != null) {
            question.setQuestionType(questionEditDTO.type());
        }

        LOGGER.info("Saving question to database.");
        Question savedQuestion = questionRepository.save(question);
        LOGGER.info("Question saved to database!");

        if (questionEditDTO.choices() != null) {
            LOGGER.info("Editing multiple choices.");
            for (MultipleChoiceDTO choice : questionEditDTO.choices()) {
                editMultipleChoiceAlternative(choice);
            }
        }

        LOGGER.info("Retrieving newest quiz.");
        return quizMapper.quizToQuizLoadDTO(quizRepository.findById(questionEditDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException(questionEditDTO.quizId().toString())));

    }

    /**
     * This method deletes a question from the database.
     * @param questionId    The question's id.
     */
    public void deleteQuestion(Long questionId) {
        LOGGER.info("Deleting question with id: " + questionId);
        //TODO: Check if owner
        Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new QuestionNotFoundException("Question with id: " + questionId));
        questionRepository.delete(question);
    }

    /**
     * This method adds a new multiple choice alternative to a question.
     *
     * @param multipleChoiceCreateDTO The information surrounding the choice.
     * @param questionId              The question to be added under.
     */
    private void addMultipleChoiceAlternative(MultipleChoiceCreateDTO multipleChoiceCreateDTO, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question"));

        MultipleChoice multipleChoice = MultipleChoiceMapper.INSTANCE.multipleChoiceCreateDTOToMultipleChoice(multipleChoiceCreateDTO);
        multipleChoice.setQuestion(question);
        multipleChoiceRepository.save(multipleChoice);
    }

    /**
     * This method edits a multiple choice alternative.
     *
     * @param multipleChoiceDTO The new information for the multiple choice alternative.
     */
    private void editMultipleChoiceAlternative(MultipleChoiceDTO multipleChoiceDTO) {
        MultipleChoice multipleChoice = multipleChoiceRepository.findById(multipleChoiceDTO.multipleChoiceId())
                .orElseThrow(() -> new QuestionNotFoundException("Multiple choice : "
                        + multipleChoiceDTO.multipleChoiceId()));
        multipleChoice.setAlternative(multipleChoiceDTO.alternative());
        multipleChoice.setCorrect(multipleChoiceDTO.isCorrect());

        multipleChoiceRepository.save(multipleChoice);
    }


}

