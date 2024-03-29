package edu.ntnu.idatt2105.backend.repo.quiz;

import edu.ntnu.idatt2105.backend.model.quiz.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * This repository creates the CRUD operations for the quiz history entity.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Repository
public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long>, JpaSpecificationExecutor<QuizHistory> {
}