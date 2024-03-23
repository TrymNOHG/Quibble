package edu.ntnu.idatt2105.backend.model.quiz;

import edu.ntnu.idatt2105.backend.model.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents feedback on a quiz.
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
@Schema(description = "Feedback on a quiz.")
@Table(name = "quiz_feedback")
public class QuizFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_feedback_id")
    @Schema(description = "The unique identifier for the author of the quiz feedback.")
    private Long quizFeedbackId;

    @Column(name = "stars")
    @Schema(description = "The number of stars left by the user on the quiz.")
    private int stars;

    @Column(name = "feedback")
    @Schema(description = "The feedback sent by the user.")
    private String feedback;

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
