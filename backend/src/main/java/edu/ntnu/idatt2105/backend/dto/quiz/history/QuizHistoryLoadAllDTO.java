package edu.ntnu.idatt2105.backend.dto.quiz.history;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

/**
 * This record retrieves all the historical quiz events.
 *
 * @param allHistoricalEvents   All the historical quiz events.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Builder
public record QuizHistoryLoadAllDTO(@NonNull List<QuizHistoryLoadDTO> allHistoricalEvents) {
}
