package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorLoadDTO;
import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper()
public interface QuizAuthorMapper {

    QuizAuthorMapper INSTANCE = Mappers.getMapper(QuizAuthorMapper.class);

    default QuizAuthorLoadAllDTO multipleQuizAuthorToDTO(Set<QuizAuthor> authors) {
        Set<QuizAuthorLoadDTO> authorLoadDTOS = authors.stream()
                .map(this::quizAuthorToQuizAuthorLoadDTO).collect(Collectors.toSet());
        return QuizAuthorLoadAllDTO.builder().collaborators(authorLoadDTOS).build();
    }

    @Mapping(target = "userId", source = "quizAuthor.user.userId")
    @Mapping(target = "quizId", source = "quizAuthor.quiz.quizId")
    @Mapping(target = "quizAuthorId", source = "quizAuthor.quizAuthorId")
    QuizAuthorLoadDTO quizAuthorToQuizAuthorLoadDTO(QuizAuthor quizAuthor);

}
