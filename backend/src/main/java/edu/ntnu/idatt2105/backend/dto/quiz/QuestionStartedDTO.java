package edu.ntnu.idatt2105.backend.dto.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record QuestionStartedDTO(
        Long id,
        String question,
        String difficulty,
        String questionType,
        List<String> options
) {}
