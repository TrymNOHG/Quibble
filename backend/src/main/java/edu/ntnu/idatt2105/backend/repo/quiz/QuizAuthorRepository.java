package edu.ntnu.idatt2105.backend.repo.quiz;

import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This repository creates the CRUD operations for the quiz author entity.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Repository
public interface QuizAuthorRepository extends JpaRepository<QuizAuthor, Long>, JpaSpecificationExecutor<QuizAuthor> {

    /**
     * This method finds a quiz author based on a quiz id and user id.
     * @param quiz_quizId  The id of the quiz.
     * @param user_userId  The id of the user.
     * @return             An optional for quiz author.
     */
    Optional<QuizAuthor> findQuizAuthorByQuizQuizIdAndUserUserId(Long quiz_quizId, Long user_userId);

}