package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackLoadDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.QuizFeedback;
import edu.ntnu.idatt2105.backend.model.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This mapper converts between the QuizFeedback model and the QuizFeedbackDTO.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 31.03.2024
 */
@Mapper()
public interface QuizFeedbackMapper {

    QuizFeedbackMapper INSTANCE = Mappers.getMapper(QuizFeedbackMapper.class);


    default QuizFeedbackLoadAllDTO multipleQuizFeedbackToDTO(Set<QuizFeedback> feedbacks) {
        Set<QuizFeedbackLoadDTO> feedbackLoadDTOS = feedbacks.stream()
                .map(this::quizFeedbackToQuizFeedbackLoadDTO).collect(Collectors.toSet());
        return QuizFeedbackLoadAllDTO.builder().feedbacks(feedbackLoadDTOS).build();
    }

    default QuizFeedback quizFeedbackDTOToQuizFeedback(QuizFeedbackDTO quizFeedbackDTO) {
        return QuizFeedback.builder()
                .quiz(Quiz.builder().quizId(quizFeedbackDTO.quizId()).build())
                .user(User.builder().userId(quizFeedbackDTO.userId()).build())
                .stars(quizFeedbackDTO.stars())
                .feedback(quizFeedbackDTO.feedback())
                .build();
    }


    @Mapping(target = "userId", source = "quizFeedback.user.userId")
    @Mapping(target = "quizId", source = "quizFeedback.quiz.quizId")
    @Mapping(target = "feedbackId", source = "quizFeedback.quizFeedbackId")
    QuizFeedbackLoadDTO quizFeedbackToQuizFeedbackLoadDTO(QuizFeedback quizFeedback);

}
