package edu.ntnu.idatt2105.backend.service.security;

import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service class for generating JWT tokens.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.1 26.03.2024
 */
@RequiredArgsConstructor
@Service
public class JWTTokenGenerationService {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final Logger log = Logger.getLogger(JWTTokenGenerationService.class.getName());

    /**
     * Generates a new access token. The access token is valid for 15 minutes. The subject is the username of the user.
     * The access token contains the user id as a claim.
     *
     * @param email The email of the user.
     * @return The access token.
     */
    public String generateToken(String email) {
        log.info("Generating JWT Access token. Email:" + email);
        Optional<User> userOptional = userRepository.findByEmail(email); // Name is email
        long userID;
        if (userOptional.isPresent()) {
            userID = userOptional.get().getUserId();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found");
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("quibble")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60*15)) // Expires in 15 min
                .subject(email) // Name is email
                .claim("id", userID)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Generates a refresh token. The refresh token is valid for 7 days and can be used to generate new access tokens.
     *
     * @param email The email of the user.
     * @return The refresh token.
     */
    public String generateRefreshToken(String email) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("quibble")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(7 * 24 * 60 * 60)) // Expires in 7 days
                .subject(email) // Name is email
                .claim("scope", "REFRESH_TOKEN")
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
