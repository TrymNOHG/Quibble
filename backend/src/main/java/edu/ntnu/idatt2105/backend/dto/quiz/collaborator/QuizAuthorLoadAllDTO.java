package edu.ntnu.idatt2105.backend.dto.quiz.collaborator;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

/**
 * This record provides a data transfer object for all collaborators of a quiz.
 *
 * @param collaborators All the collaborators for a given quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Builder
public record QuizAuthorLoadAllDTO(@NonNull Set<QuizAuthorLoadDTO> collaborators) {
}