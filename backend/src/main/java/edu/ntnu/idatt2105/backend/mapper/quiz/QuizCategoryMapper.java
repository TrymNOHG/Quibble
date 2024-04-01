package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.category.QuizCategoryLoadDTO;
import edu.ntnu.idatt2105.backend.model.category.QuizCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface QuizCategoryMapper {

    QuizCategoryMapper INSTANCE = Mappers.getMapper(QuizCategoryMapper.class);

    @Mapping(target = "quizId", source = "quizCategory.quiz.quizId")
    @Mapping(target = "categoryDescription", source = "quizCategory.category.categoryDescription")
    @Mapping(target = "category", source = "quizCategory.category.categoryName")
    QuizCategoryLoadDTO quizCategoryToQuizCategoryLoadDTO(QuizCategory quizCategory);

}
