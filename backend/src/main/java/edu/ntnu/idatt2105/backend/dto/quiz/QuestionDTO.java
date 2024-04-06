package edu.ntnu.idatt2105.backend.dto.quiz;

import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceDTO;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record QuestionDTO(
      @NonNull  Long id,
      @NonNull  String question,
      @NonNull  String answer,
      @NonNull  String questionType,
      @NonNull  List<MultipleChoiceDTO> options
) {}
