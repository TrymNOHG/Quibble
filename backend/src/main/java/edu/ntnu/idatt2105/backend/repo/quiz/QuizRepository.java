package edu.ntnu.idatt2105.backend.repo.quiz;

import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This repository creates the CRUD operations for the quiz entity.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>, JpaSpecificationExecutor<Quiz> {

    /**
     * This method finds a quiz by its name.
     *
     * @param quizName The name of the quiz.
     * @return The quiz with the given name.
     */
    Optional<Quiz> findByQuizName(String quizName);
}
