package edu.ntnu.idatt2105.backend.repo.quiz.question;


import edu.ntnu.idatt2105.backend.model.quiz.question.MultipleChoice;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * This repository creates the CRUD operations for the multiple choice entity.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Repository
public interface MultipleChoiceRepository extends JpaRepository<MultipleChoice, Long>, JpaSpecificationExecutor<MultipleChoice> {

    /**
     * This method finds all multiple choices belonging to a question.
     * @param questionId    The id of the question.
     * @return              Optional, potentially containing set of multiple choices.
     */
    Optional<Set<MultipleChoice>> findMultipleChoicesByQuestionQuestionId(@NonNull Long questionId);

}