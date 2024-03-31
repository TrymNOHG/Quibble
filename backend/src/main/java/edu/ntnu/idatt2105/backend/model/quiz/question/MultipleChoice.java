package edu.ntnu.idatt2105.backend.model.quiz.question;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents one choice for a multiple choice question.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 22.03.2024
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Schema(description = "The choices for multiple choice question.")
@Table(name = "multiple_choice")
public class MultipleChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mult_choice_id")
    @Schema(description = "The unique identifier for the multiple choice answer.")
    private Long multipleChoiceId;

    @Column(name = "altenrative", nullable = false)
    @NonNull
    @Schema(description = "An option to the question.")
    private String alternative;

    @Column(name = "is_correct", nullable = false)
    @Schema(description = "Whether the option is correct.")
    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    @ToString.Exclude
    @Schema(description = "The question the choice belongs to.")
    private Question question;
}
