package edu.ntnu.idatt2105.backend.mapper.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.feedback.QuizFeedbackLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.keyword.QuizKeywordLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import java.util.stream.Collectors;

/**
 * This class creates mapping between the Quiz model and its respective DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Mapper(componentModel = "spring")
public interface QuizMapper {

    //TODO: multiple quizzes


    default QuizLoadDTO quizToQuizLoadDTO(Quiz quiz) {
        return QuizLoadDTO
                .builder()
                .quizId(quiz.getQuizId())
                .difficulty(quiz.getDifficulty())
                .quizDescription(quiz.getQuizDescription())
                .quizName(quiz.getQuizName())
                .adminId(quiz.getAdmin().getUserId())
                .categories(quiz
                        .getCategories()
                        .stream()
                        .map(QuizCategoryMapper.INSTANCE::quizCategoryToQuizCategoryLoadDTO)
                        .collect(Collectors.toSet())
                )
                .collaborators(quiz
                        .getCollaborators()
                        .stream()
                        .map(QuizAuthorMapper.INSTANCE::quizAuthorToQuizAuthorDTO)
                        .collect(Collectors.toSet()))
                .keywords(quiz
                        .getKeywords()
                        .stream()
                        .map(QuizKeywordMapper.INSTANCE::quizKeywordToQuizKeywordLoadDTO)
                        .collect(Collectors.toSet())
                )
                .feedbacks(quiz
                        .getFeedbacks()
                        .stream()
                        .map(QuizFeedbackMapper.INSTANCE::quizFeedbackToQuizFeedbackLoadDTO)
                        .collect(Collectors.toSet()))
                .questions(quiz
                        .getQuestions()
                        .stream()
                        .map(QuestionMapper.INSTANCE::questionToQuestionDTO)
                        .collect(Collectors.toSet()))
                .build();
    }


}