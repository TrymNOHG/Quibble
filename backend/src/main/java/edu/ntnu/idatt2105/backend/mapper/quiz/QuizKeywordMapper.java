package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.keyword.QuizKeywordLoadDTO;
import edu.ntnu.idatt2105.backend.model.quiz.QuizKeyword;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * This mapper converts between the quiz keyword model and data transfer objects.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 01.04.2024
 */
@Mapper()
public interface QuizKeywordMapper {

    QuizKeywordMapper INSTANCE = Mappers.getMapper(QuizKeywordMapper.class);


    @Mapping(target = "quizId", source = "quizKeyword.quiz.quizId")
    @Mapping(target = "keywordId", source = "quizKeyword.quizKeywordId")
    QuizKeywordLoadDTO quizKeywordToQuizKeywordLoadDTO(QuizKeyword quizKeyword);

}
