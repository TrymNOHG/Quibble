package edu.ntnu.idatt2105.backend.model.users;

import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import edu.ntnu.idatt2105.backend.model.quiz.QuizFeedback;
import edu.ntnu.idatt2105.backend.model.quiz.QuizHistory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

/**
 User class represents a user of the e-commerce application with their personal information,
 authentication details, and activities on the platform.

 @author Trym Hamer Gudvangen
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Schema(description = "A user of the web application.")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Schema(description = "The unique identifier for the user")
    private Long userId;

    @Column(name = "username", length = 64, nullable = false, unique = true)
    @NonNull
    @Schema(description = "The username of the user, must be unique and not null")
    private String username;

    @Column(name = "password", nullable = false)
    @NonNull
    @Schema(description = "The salted and hashed password of the user, not null.")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @NonNull
    @Schema(description = "The user's email address, not null.")
    private String email;

    @Column(name = "profile_picture")
    @Schema(description = "A link to the user's profile picture.")
    private String profilePicLink;

    @OneToMany(mappedBy = "admin")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The quizzes created by the user.")
    private Set<Quiz> quizzesOwned = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The user's tokens.")
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The user's quiz history.")
    private Set<QuizHistory> quizHistory = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The user's feedbacks.")
    private Set<QuizFeedback> feedbacks = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @Schema(description = "The user's collaborations on quizzes.")
    private Set<QuizAuthor> collaborators = new HashSet<>();
}
