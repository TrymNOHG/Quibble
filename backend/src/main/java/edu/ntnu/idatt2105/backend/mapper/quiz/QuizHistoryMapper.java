package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.history.QuizHistoryLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.history.QuizHistoryLoadDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.QuizFeedback;
import edu.ntnu.idatt2105.backend.model.quiz.QuizHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This mapper converts between quiz history models and DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 31.03.2024
 */
@Mapper()
public interface QuizHistoryMapper {

    QuizHistoryMapper INSTANCE = Mappers.getMapper(QuizHistoryMapper.class);

    /**
     * This method maps multiple history model objects to history load DTOs.
     * @param histories     History models.
     * @return              Set of histroy load DTOs.
     */
    default QuizHistoryLoadAllDTO multipleQuizHistoryToDTO(Set<QuizHistory> histories) {
        Set<QuizHistoryLoadDTO> quizHistoryLoadDTOS = histories.stream()
                .map(this::quizHistoryToQuizHistoryLoadDTO).collect(Collectors.toSet());
        return QuizHistoryLoadAllDTO.builder().allHistoricalEvents(quizHistoryLoadDTOS).build();
    }

    /**
     * This method maps a quiz history model to a quiz history DTO.
     * @param quizHistory   Quiz history model.
     * @return              Quiz history DTO.
     */
    @Mapping(target = "userId", source = "quizHistory.user.userId")
    @Mapping(target = "quizId", source = "quizHistory.quiz.quizId")
    QuizHistoryLoadDTO quizHistoryToQuizHistoryLoadDTO(QuizHistory quizHistory);

}
