package edu.ntnu.idatt2105.backend.dto.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.keyword.QuizKeywordLoadDTO;
import edu.ntnu.idatt2105.backend.model.category.Category;
import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * This record provides a data transfer object for filtering quizzes.
 *
 * @param name              The fuzzy name search for quiz.
 * @param difficulties      The difficulties to include.
 * @param categories        The categories to include.
 * @param keywords          The keywords to look for.
 * @param minStars          Minimum number of stars to look for.
 * @param pageSize          Number of quizzes to retrieve.
 * @param pageNumber        The current page..
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 04.04.2024
 */
@Builder
public record QuizFilterDTO(@Nullable String name,
                            @Nullable Set<Difficulty> difficulties,
                            @Nullable Set<String> categories,
                            @Nullable Set<QuizKeywordLoadDTO> keywords,
                            @Nullable Long minStars,
                            int pageSize,
                            int pageNumber
) {}
