package edu.ntnu.idatt2105.backend.model.quiz;

import edu.ntnu.idatt2105.backend.model.category.QuizCategory;
import edu.ntnu.idatt2105.backend.model.quiz.question.Question;
import edu.ntnu.idatt2105.backend.model.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the quiz entity.
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
@Schema(description = "A single quiz.")
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    @Schema(description = "The unique identifier for the quiz")
    private Long quizId;

    @Column(name = "quiz_name", length = 64, nullable = false)
    @NonNull
    @Schema(description = "The name of the quiz, must be unique and not null")
    private String quizName;

    @Column(name = "quiz_desc", length = 255)
    @NonNull
    @Schema(description = "The description of the quiz.")
    private String quizDescription;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @Schema(description = "The user who created the quiz.")
    private User admin;

    @OneToMany(mappedBy = "quiz")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The quiz's history.")
    private Set<QuizHistory> quizHistory = new HashSet<>();

    @OneToMany(mappedBy = "quiz")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The quiz's feedbacks.")
    private Set<QuizFeedback> feedbacks = new HashSet<>();

    @OneToMany(mappedBy = "quiz")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The collaborators on the quiz.")
    private Set<QuizAuthor> collaborators = new HashSet<>();

    @OneToMany(mappedBy = "quiz")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The quiz's categories.")
    private Set<QuizCategory> categories = new HashSet<>();

    @OneToMany(mappedBy = "quiz")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The quiz's questions.")
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "quiz")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The quiz's keywords.")
    private Set<QuizKeyword> keywords = new HashSet<>();

}