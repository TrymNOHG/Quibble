package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;



@RequiredArgsConstructor
@Service
public class JWTTokenService {

    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("quibble")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60*15)) // Expires in 15 min
                .subject(authentication.getName())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Generates a refresh token. The refresh token is valid for 7 days and can be used to generate new access tokens.
     *
     * @param authentication The authentication object containing the user details.
     * @return The refresh token.
     */
    public String generateRefreshToken(Authentication authentication) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("quibble")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(7 * 24 * 60 * 60)) // Expires in 7 days
                .subject(authentication.getName())
                .claim("scope", "REFRESH_TOKEN")
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
