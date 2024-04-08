package edu.ntnu.idatt2105.backend.dto.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceDTO;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

/**
 * This record provides a data transfer object for question.
 * @param id            The id of the question.
 * @param question      The actual question.
 * @param answer        The answer.
 * @param questionType  The type of question.
 * @param options       The choices.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 04.04.2024
 */
@Builder
public record QuestionDTO(
      @NonNull  Long id,
      @NonNull  String question,
      @NonNull  String answer,
      @NonNull  String questionType,
      @NonNull  List<MultipleChoiceDTO> options
) {}
