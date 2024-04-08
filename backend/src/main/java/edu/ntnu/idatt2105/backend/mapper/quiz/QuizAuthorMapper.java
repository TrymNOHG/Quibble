package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorLoadDTO;
import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This mapper converts between the quiz author model and DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 06.04.2024
 */
@Mapper()
public interface QuizAuthorMapper {

    QuizAuthorMapper INSTANCE = Mappers.getMapper(QuizAuthorMapper.class);

    /**
     * This method converts between multiple quiz authors and a QuizAuthorLoadAllDTO.
     * @param authors   Quiz authors to convert.
     * @return          The quiz author load all data transfer object.
     */
    default QuizAuthorLoadAllDTO multipleQuizAuthorToDTO(Set<QuizAuthor> authors) {
        Set<QuizAuthorLoadDTO> authorLoadDTOS = authors.stream()
                .map(this::quizAuthorToQuizAuthorLoadDTO).collect(Collectors.toSet());
        return QuizAuthorLoadAllDTO.builder().collaborators(authorLoadDTOS).build();
    }

    /**
     * This method converts between a quiz author object to a quiz author load DTO.
     * @param quizAuthor    The quiz author to convert.
     * @return              The resulting quiz author load DTO.
     */
    @Mapping(target = "username", source = "quizAuthor.user.username")
    @Mapping(target = "userId", source = "quizAuthor.user.userId")
    @Mapping(target = "quizId", source = "quizAuthor.quiz.quizId")
    @Mapping(target = "quizAuthorId", source = "quizAuthor.quizAuthorId")
    QuizAuthorLoadDTO quizAuthorToQuizAuthorLoadDTO(QuizAuthor quizAuthor);

}
