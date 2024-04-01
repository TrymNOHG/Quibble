package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface QuestionMapper {

    default Question questionCreateDTOToQuestion(QuestionCreateDTO questionCreateDTO) {
        return Question
                .builder()
                .questionType(questionCreateDTO.type())
                .answer(questionCreateDTO.answer())
                .question(questionCreateDTO.question())
                .build();
    }

}
