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
@Table(name = "refresh_tokens")
@Schema(description = "A user's token, used for authorization when interacting with the application.")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    @Schema(description = "The unique identifier for the token.")
    private Long tokenId;

    @Column(name = "token", length = 10000, nullable = false)
    @NonNull
    @Schema(description = "The actual token value, must be non-null.")
    private String token;

    @Column(name = "revoked", nullable = false)
    @Schema(description = "Whether the token has been revoked or not.")
    private boolean revoked;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @NonNull
    @ToString.Exclude
    @Schema(description = "The user the token is connected to.")
    private User user;

}
