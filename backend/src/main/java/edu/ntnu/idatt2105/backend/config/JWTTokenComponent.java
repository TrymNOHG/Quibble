package edu.ntnu.idatt2105.backend.config;

import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class JWTTokenComponent {
    final private UserRepository userRepository;
    /**
     * Gets the username from a JWT token.
     *
     * @param jwt The JWT token.
     * @return The username.
     */
    public String getUserName(Jwt jwt) {
        return jwt.getSubject();
    }

    public UserDetails getUserDetails(String username) throws Exception {
        return userRepository.findByUsername(username).map(UserConfig::new)
                .orElseThrow(()-> new Exception("User not found")); //TODO: another exception
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isAfter(Instant.now())
                && jwtToken.getSubject().equals(userDetails.getUsername());
    }

}
