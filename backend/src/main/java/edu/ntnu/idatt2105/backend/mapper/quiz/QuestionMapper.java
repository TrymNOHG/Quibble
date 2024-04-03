package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionDTO;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;


@Mapper()
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    default Question questionCreateDTOToQuestion(QuestionCreateDTO questionCreateDTO) {
        return Question
                .builder()
                .questionType(questionCreateDTO.type())
                .answer(questionCreateDTO.answer())
                .question(questionCreateDTO.question())
                .build();
    }

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
