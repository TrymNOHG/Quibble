package edu.ntnu.idatt2105.backend.service.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuestionDTO;
import edu.ntnu.idatt2105.backend.dto.websocket.SendAlternativesDTO;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Service class for handling questions.
 *
 * @version 1.0 31.05.2021
 * @author brage
 * @see Question
 */
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    Logger log = Logger.getLogger(QuestionService.class.getName());

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
                .answer(question.getAnswer())
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
        if(question.getAnswer() == null || question.getAnswer().isBlank()) {
            return question.getChoices().stream().filter(MultipleChoice::isCorrect).findFirst().orElseThrow(
                    () -> new IllegalStateException("No correct answer found for question " + question.getQuestionId())
            ).getAlternative();
        }
        log.info("Question: " + question.getQuestion() + " Answer: " + question.getAnswer());
        return question.getAnswer();
    }
}
