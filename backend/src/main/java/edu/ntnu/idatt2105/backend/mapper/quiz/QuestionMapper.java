package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionDTO;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

/**
 * This mapper converts between the question model and DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 06.04.2024
 */
@Mapper()
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    /**
     * This method converts between the question create DTO and the question model.
     * @param questionCreateDTO     Question create data transfer object.
     * @return                      Question model.
     */
    default Question questionCreateDTOToQuestion(QuestionCreateDTO questionCreateDTO) {
        return Question
                .builder()
                .questionType(questionCreateDTO.type())
                .answer(questionCreateDTO.answer())
                .question(questionCreateDTO.question())
                .build();
    }

    /**
     * This method converts between the question model and the question data transfer object.
     * @param question  Question to convert.
     * @return          Question DTO.
     */
    default QuestionDTO questionToQuestionDTO(Question question) {
        return QuestionDTO
                .builder()
                .questionId(question.getQuestionId())
                .question(question.getQuestion())
                .answer(question.getAnswer())
                .type(question.getQuestionType())
                .choices(question
                        .getChoices()
                        .stream()
                        .map(MultipleChoiceMapper.INSTANCE::multipleChoiceToMultipleChoiceDTO)
                        .collect(Collectors.toSet())
                )
                .quizId(question.getQuiz().getQuizId())
                .build();
    }

}
