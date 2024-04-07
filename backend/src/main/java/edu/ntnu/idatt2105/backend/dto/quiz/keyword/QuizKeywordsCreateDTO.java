package edu.ntnu.idatt2105.backend.dto.quiz.keyword;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

/**
 * This record provides a data transfer object for quiz keywords.
 * @param quizId        The id of the quiz.
 * @param keywords      The keywords to be added.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 07.04.2024
 */
@Builder
public record QuizKeywordsCreateDTO(@NonNull Long quizId, @NonNull List<String> keywords) {
}
