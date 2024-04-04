package edu.ntnu.idatt2105.backend.specification.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.QuizFilterDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * This class defines custom specifications for retrieval of quizzes. This functionality includes a specification for
 * quizzes filtered by name, categories, difficulties, and keywords.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 04.04.2024
 */
public class QuizSpecification {

    /**
     * This method creates a specification for filtering quizzes based on name, category, difficulty, and keywords.
     * @param quizFilterDTO The filter quiz data transfer object.
     * @return              The filter specification.
     */
    public static Specification<Quiz> filterQuizzes(QuizFilterDTO quizFilterDTO) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (quizFilterDTO.name() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + quizFilterDTO.name() + "%"));
            }

            if (quizFilterDTO.difficulties() != null && !quizFilterDTO.difficulties().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("difficulty").in(quizFilterDTO.difficulties()));
            }

            if (quizFilterDTO.categories() != null && !quizFilterDTO.categories().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("quizCategories").get("category").get("categoryName").in(quizFilterDTO.categories()));
            }

            //TODO: make filtering based on keywords better. Maybe use fuzzy or just to order the quizzes retrieved from the rest.
            if (quizFilterDTO.keywords() != null && !quizFilterDTO.keywords().isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("keywords").get("keyword").in(quizFilterDTO.keywords()));
            }

            return predicate;
        };
    }

}
