package edu.ntnu.idatt2105.backend.dto.quiz;

import edu.ntnu.idatt2105.backend.model.category.QuizCategory;
import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import edu.ntnu.idatt2105.backend.model.quiz.QuizFeedback;
import edu.ntnu.idatt2105.backend.model.quiz.QuizKeyword;
import edu.ntnu.idatt2105.backend.model.quiz.question.Difficulty;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import lombok.Builder;
import lombok.NonNull;

import java.util.Set;

/**
 * This Data Transfer Object contains the information for loading a quiz.
 *
 * @param quizId            The id of the quiz.
 * @param quizName          The name of the quiz.
 * @param quizDescription   The description of the quiz.
 * @param admin_id          The id of the admin of the quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Builder
public record QuizLoadDTO(
        @NonNull Long quizId, @NonNull String quizName, String quizDescription, @NonNull Difficulty difficulty,
        @NonNull Long admin_id, Set<QuizFeedback> feedbacks, Set<QuizAuthor> collaborators,
        Set<QuizCategory> categories, Set<Question> questions, Set<QuizKeyword> keywords
) {}