package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceDTO;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MultipleChoiceMapper {


    default MultipleChoice multipleChoiceDTOToMultipleChoice(MultipleChoiceDTO multipleChoiceDTO) {
        return MultipleChoice
                .builder()
                .multipleChoiceId(multipleChoiceDTO.multipleChoiceId())
                .alternative(multipleChoiceDTO.alternative())
                .isCorrect(multipleChoiceDTO.isCorrect())
                .build();
    }

}
