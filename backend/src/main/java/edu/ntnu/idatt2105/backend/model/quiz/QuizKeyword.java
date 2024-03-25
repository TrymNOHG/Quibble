package edu.ntnu.idatt2105.backend.model.quiz;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents one keyword connected to a quiz.
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
@Schema(description = "A single keyword for a quiz.")
@Table(name = "quiz_keyword")
public class QuizKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_keyword_id")
    @Schema(description = "The unique identifier for the quiz keyword")
    private Long quizKeywordId;

    @Column(name = "keyword", nullable = false)
    @NonNull
    @Schema(description = "The keyword.")
    private String keyword;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "quiz_id")
    @ToString.Exclude
    @Schema(description = "The quiz the keyword belongs to.")
    private Quiz quiz;

}
