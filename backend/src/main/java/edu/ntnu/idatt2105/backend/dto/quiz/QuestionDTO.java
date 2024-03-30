package edu.ntnu.idatt2105.backend.dto.quiz;

import lombok.Builder;

import java.util.List;

@Builder
public record QuestionDTO(
        Long id,
        String question,
        String answer,
        String difficulty,
        String questionType,
        List<String> options
) {}
