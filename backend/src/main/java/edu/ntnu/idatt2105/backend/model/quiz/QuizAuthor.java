package edu.ntnu.idatt2105.backend.model.quiz;

import edu.ntnu.idatt2105.backend.model.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents a collaborator on a specific quiz.
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
@Schema(description = "A collaborator on the quiz.")
@Table(name = "quiz_author")
public class QuizAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_author_id")
    @Schema(description = "The unique identifier for the author of the quiz.")
    private Long quizAuthorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    @ToString.Exclude
    @Schema(description = "The quiz being collaborated on.")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @Schema(description = "The collaborator.")
    private User user;
}
