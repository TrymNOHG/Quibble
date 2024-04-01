package edu.ntnu.idatt2105.backend.dto.quiz.keyword;

import lombok.Builder;
import lombok.NonNull;

/**
 * This record provides a data transfer object of a keyword for a given quiz.
 * @param keywordId     The id of the keyword.
 * @param keyword       The actual keyword.
 * @param quizId        The id of the quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 01.04.2024
 */
@Builder
public record QuizKeywordLoadDTO(
        @NonNull Long keywordId,
        @NonNull String keyword,
        @NonNull Long quizId
) {}
