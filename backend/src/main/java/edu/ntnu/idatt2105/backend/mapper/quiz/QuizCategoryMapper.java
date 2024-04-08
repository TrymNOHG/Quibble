package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryLoadDTO;
import edu.ntnu.idatt2105.backend.model.category.QuizCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * This mapper converts between the quiz category model and the respective data transfer objects.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 06.04.2024
 */
@Mapper()
public interface QuizCategoryMapper {

    QuizCategoryMapper INSTANCE = Mappers.getMapper(QuizCategoryMapper.class);

    /**
     * This method converts between the quiz category object to the quiz category load DTO.
     * @param quizCategory  The quiz category model.
     * @return              The resulting quiz category load DTO.
     */
    @Mapping(target = "quizId", source = "quizCategory.quiz.quizId")
    @Mapping(target = "categoryDescription", source = "quizCategory.category.categoryDescription")
    @Mapping(target = "category", source = "quizCategory.category.categoryName")
    QuizCategoryLoadDTO quizCategoryToQuizCategoryLoadDTO(QuizCategory quizCategory);

}
