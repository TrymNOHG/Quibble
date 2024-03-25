package edu.ntnu.idatt2105.backend.model.quiz;

import edu.ntnu.idatt2105.backend.model.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This class represents the historical data of a user for a given quiz. In other words, it represents a single
 * interaction between a user and a quiz.
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
@Schema(description = "Historical interaction of user on a quiz.")
@Table(name = "quiz_history")
public class QuizHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_history_id")
    @Schema(description = "The unique identifier for the historical event.")
    private Long quizHistoryId;

    @Column(name = "score")
    @Schema(description = "The score the user received on the quiz.")
    private double score;

    @Column(name = "timestamp", nullable = false)
    @NonNull
    @Schema(description = "The time the interaction was recorded.")
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "quiz_id")
    @ToString.Exclude
    @Schema(description = "The quiz the feedback is being added to.")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @Schema(description = "The user adding the feedback.")
    private User user;

}
