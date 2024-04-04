package edu.ntnu.idatt2105.backend.model.category;

import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents one of the categories for a quiz.
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
@Schema(description = "The category connected to a quiz.")
@Table(name = "quiz_category")
public class QuizCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_category_id")
    @Schema(description = "The unique identifier for the quiz category")
    private Long quizCategoryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    @Schema(description = "The specific category.")
    private Category category;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    @ToString.Exclude
    @Schema(description = "The quiz the category belongs to.")
    private Quiz quiz;

}
