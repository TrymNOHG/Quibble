package edu.ntnu.idatt2105.backend.model.quiz.question;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents one choice for a ranked question.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 22.03.2024
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Schema(description = "The choices for multiple choice question.")
@Table(name = "ranked_choice")
public class RankedChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_id")
    @Schema(description = "The unique identifier for the ranked choice option.")
    private Long rankedChoiceId;

    @Column(name = "rank_pos")
    @Schema(description = "The position of the ranking.")
    private int position;

    @Column(name = "option", nullable = false)
    @NonNull
    @Schema(description = "An option to the question.")
    private String option;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    @ToString.Exclude
    @Schema(description = "The question the choice belongs to.")
    private Question question;

}
