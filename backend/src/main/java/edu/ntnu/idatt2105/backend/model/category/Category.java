package edu.ntnu.idatt2105.backend.model.category;

import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the category entity.
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
@Schema(description = "A category for quizzes.")
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    @Schema(description = "The unique identifier for the cateogry")
    private Long categoryId;

    @Column(name = "category_name", length = 64, nullable = false)
    @NonNull
    @Schema(description = "The name of the category, must be unique and not null")
    private String categoryName;

    @Column(name = "category_desc", length = 255)
    @NonNull
    @Schema(description = "The description of the category.")
    private String categoryDescription;

    @OneToMany(mappedBy = "category")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The quiz's the category is a part of.")
    private Set<QuizCategory> quizCategories = new HashSet<>();

}
