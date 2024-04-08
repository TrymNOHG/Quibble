package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceDTO;
import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * This mapper converts between multiple choice models and respective data transfer objects.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 06.04.2024
 */
@Mapper()
public interface MultipleChoiceMapper {

    MultipleChoiceMapper INSTANCE = Mappers.getMapper(MultipleChoiceMapper.class);

    /**
     * This method provides a way to convert between multiple choice DTOs to multiple choice object.
     * @param multipleChoiceDTO     The multiple choice DTO.
     * @return                      The resulting multiple choice object.
     */
    default MultipleChoice multipleChoiceDTOToMultipleChoice(MultipleChoiceDTO multipleChoiceDTO) {
        return MultipleChoice
                .builder()
                .multipleChoiceId(multipleChoiceDTO.multipleChoiceId())
                .alternative(multipleChoiceDTO.alternative())
                .isCorrect(multipleChoiceDTO.isCorrect())
                .build();
    }

    /**
     * This method provides a way to convert between multiple choice create DTOs to multiple choice object.
     * @param multipleChoiceCreateDTO       The multiple choice create DTO.
     * @return                              The resulting multiple choice object.
     */
    default MultipleChoice multipleChoiceCreateDTOToMultipleChoice(MultipleChoiceCreateDTO multipleChoiceCreateDTO) {
        return MultipleChoice
                .builder()
                .alternative(multipleChoiceCreateDTO.alternative())
                .isCorrect(multipleChoiceCreateDTO.isCorrect())
                .build();
    }


    /**
     * This method provides a way to convert between multiple choice object to multiple choice DTO.
     * @param multipleChoice        The multiple choice.
     * @return                      The resulting multiple choice DTO.
     */
    default MultipleChoiceDTO multipleChoiceToMultipleChoiceDTO(MultipleChoice multipleChoice) {
        return MultipleChoiceDTO
                .builder()
                .multipleChoiceId(multipleChoice.getMultipleChoiceId())
                .alternative(multipleChoice.getAlternative())
                .isCorrect(multipleChoice.isCorrect())
                .questionId(multipleChoice.getQuestion().getQuestionId())
                .build();
    }

}
