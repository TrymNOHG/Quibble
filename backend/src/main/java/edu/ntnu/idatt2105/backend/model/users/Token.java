package edu.ntnu.idatt2105.backend.model.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This class represents a user token.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "tokens")
@Schema(description = "A user's token, used for authorization when interacting with the application.")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    @Schema(description = "The unique identifier for the token.")
    private Long tokenId;

    @Column(name = "token", length = 64, nullable = false)
    @NonNull
    @Schema(description = "The actual token value, must be non-null.")
    private String token;

    @Column(name = "time_created", length = 64, nullable = false)
    @NonNull
    @Schema(description = "When the token was created, must be non-null.")
    private LocalDateTime timeCreated;

    @Column(name = "time_expired", length = 64, nullable = false)
    @NonNull
    @Schema(description = "When the token was expires, must be non-null.")
    private LocalDateTime timeExpired;

    @Column(name = "time_confirmed", length = 64)
    @Schema(description = "Confirmation time of the token.")
    private LocalDateTime timeConfirmed;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @NonNull
    @ToString.Exclude
    @Schema(description = "The user the token is connected to.")
    private User user;


}
