package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceDTO;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface MultipleChoiceMapper {

    MultipleChoiceMapper INSTANCE = Mappers.getMapper(MultipleChoiceMapper.class);

    default MultipleChoice multipleChoiceDTOToMultipleChoice(MultipleChoiceDTO multipleChoiceDTO) {
        return MultipleChoice
                .builder()
                .multipleChoiceId(multipleChoiceDTO.multipleChoiceId())
                .alternative(multipleChoiceDTO.alternative())
                .isCorrect(multipleChoiceDTO.isCorrect())
                .build();
    }

}
