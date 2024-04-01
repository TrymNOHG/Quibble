package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * This class creates mapping between the Quiz model and its respective DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Mapper(componentModel = "spring")
public interface QuizMapper {

    //TODO: multiple quizzes

    @Mapping(target = "admin_id", source = "admin.userId")
    QuizLoadDTO quizToQuizLoadDTO(Quiz quiz);

}
