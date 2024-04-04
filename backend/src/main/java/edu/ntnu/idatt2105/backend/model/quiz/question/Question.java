package edu.ntnu.idatt2105.backend.model.quiz.question;


import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

/**
 * The Question class represents a question for a given quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Schema(description = "One question for a given quiz.")
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    @Schema(description = "The unique identifier for the quiz question.")
    private Long questionId;

    @Column(name = "question", nullable = false)
    @NonNull
    @Schema(description = "The actual question.")
    private String question;

    @Column(name = "answer")
    @Schema(description = "The answer to the question.")
    private String answer;

    @Column(name = "type", nullable = false)
    @NonNull
    @Schema(description = "The type of question.")
    private QuestionType questionType;

    @OneToMany(mappedBy = "question")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The quiz's history.")
    @Builder.Default
    private Set<MultipleChoice> choices = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    @ToString.Exclude
    @Schema(description = "The quiz the question belongs to.")
    private Quiz quiz;
}
