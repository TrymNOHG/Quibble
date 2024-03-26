package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.config.UserConfig;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class JWTTokenService {

    final private UserRepository userRepository;
    /**
     * Gets the email from a JWT token.
     *
     * @param jwt The JWT token.
     * @return The enail of the user.
     */
    public String getUserEmail(Jwt jwt) {
        return jwt.getSubject();
    }

    /**
     * Gets the user id from a JWT token.
     *
     * @param jwt The JWT token.
     * @return The user id.
     */
    public String getUserId(Jwt jwt) {
        return jwt.getClaim("id");
    }

    /**
     * Gets the user details from the email.
     *
     * @param email The email of the user.
     * @return The user details.
     * @throws IOException If the user is not found.
     */
    public UserDetails getUserDetails(String email) throws IOException {
        return userRepository
                .findByEmail(email)
                .map(UserConfig::new)
                .orElseThrow(()-> new IOException("User not found"));
    }

    /**
     * Checks if the token is valid. The token is valid if it is not expired and the subject
     * is the same as the email of the user.
     *
     * @param jwtToken The JWT token.
     * @param userDetails The user details.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isAfter(Instant.now())
                && jwtToken.getSubject().equals(userDetails.getUsername());
    }
}
