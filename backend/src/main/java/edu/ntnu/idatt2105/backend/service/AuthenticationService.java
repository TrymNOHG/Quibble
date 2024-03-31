package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.dto.security.AuthenticationResponseDTO;
import edu.ntnu.idatt2105.backend.dto.security.TokenType;
import edu.ntnu.idatt2105.backend.dto.users.UserRegisterDTO;
import edu.ntnu.idatt2105.backend.model.users.RefreshToken;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.RefreshTokenRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.logging.Logger;

/**
 * Service for handling authentication logic for the application.
 *
 * @author brage
 * @version 1.1 26.03.2024
 */
@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JWTTokenGenerationService jwtTokenGenerationService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    private final Logger log = Logger.getLogger(AuthenticationService.class.getName());

    /**
     * Gets refresh and access token from the spring security authentication object. The refresh tokens is set as a
     * cookie.
     *
     * @param authentication The authentication object containing the user details.
     * @param httpServletResponse The http response object.
     * @return AuthenticationResponseDTO containing the access token.
     */
    public AuthenticationResponseDTO getTokensFromAuth(
            Authentication authentication, HttpServletResponse httpServletResponse
    ) {
        try {
            log.info("Getting tokens from authentication object. Name of authentication: " + authentication.getName());
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(
                    ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
            );
            // Check username as well...

            String refreshToken = jwtTokenGenerationService.generateRefreshToken(user.getEmail());


            RefreshToken refreshTokenEntity = RefreshToken.builder()
                    .user(user)
                    .token(refreshToken)
                    .revoked(false)
                    .build();
            refreshTokenRepository.save(refreshTokenEntity);

            setRefreshTokenCookie(httpServletResponse, refreshToken);

            return AuthenticationResponseDTO.builder()
                    .token(jwtTokenGenerationService.generateToken(user.getEmail()))
                    .tokenExpiration(60*15)
                    .username(user.getEmail())
                    .tokenType(TokenType.Bearer).build();

        } catch (Exception e) {
            log.warning(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while getting token"
            );
        }
    }

    /**
     * Registers a new user in the database. The user is saved with a hashed password.
     * The user is also given a refresh token and an access token.
     * The refresh token is set as a cookie.
     * The access token is returned in the response.
     *
     * @param userRegistrationDto The user registration dto containing the user information.
     * @param httpServletResponse The http response object.
     * @return AuthenticationResponseDTO containing the access token.
     */
    public AuthenticationResponseDTO registerUser(UserRegisterDTO userRegistrationDto,
                                                  HttpServletResponse httpServletResponse) {

        if (userRegistrationDto.username().length() < 3 || userRegistrationDto.username().length() > 64
                || userRegistrationDto.password().length() < 8 || userRegistrationDto.password().length() > 64) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Username or password is invalid, too short or too long.");
        }

        if (!userRegistrationDto.email().matches("^(.+)@(.+)$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
        }

        String profilePicLink;
        if (userRegistrationDto.profilePicLink() != null && !userRegistrationDto.profilePicLink().matches("^(http|https)://.*$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid profile picture link");
        } else {
            profilePicLink = "profile_pic.PNG";
        }

        log.info("Registering user with email: " + userRegistrationDto.email());
        if (userRepository.findByUsername(userRegistrationDto.username()).isPresent()
                || userRepository.findByEmail(userRegistrationDto.email()).isPresent()) {
            log.info("User already exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User Already Exist");
        }
        try{
            log.info("User does not exist, creating a user object");
            log.info(userRegistrationDto.toString());
            log.info("Profilepiclink: " + profilePicLink);
            User newUser = User.builder()
                    .email(userRegistrationDto.email())
                    .username(userRegistrationDto.username())
                    .password(passwordEncoder.encode(userRegistrationDto.password()))
                    .profilePicLink(profilePicLink) // TODO: Nå kan man legge inn hva som helst av linker. Er det sketchy?
                    .build();
            log.info("User object created successfully");
            Authentication authentication = createAuthenticationObject(newUser);
            User savedUser = userRepository.save(newUser);

            log.info("Creating image directory for user.");
            imageService.initializeUserDir(savedUser.getUserId());

            String accessToken = jwtTokenGenerationService.generateToken(savedUser.getEmail());
            String refreshToken = jwtTokenGenerationService.generateRefreshToken(savedUser.getEmail());
            log.info("Tokens created successfully");
            RefreshToken refreshTokenEntity = RefreshToken.builder()
                    .user(savedUser)
                    .token(refreshToken)
                    .revoked(false)
                    .build();
            refreshTokenRepository.save(refreshTokenEntity);

            setRefreshTokenCookie(httpServletResponse, refreshToken);
            log.info("User created successfully");
            return AuthenticationResponseDTO.builder()
                    .token(accessToken)
                    .tokenExpiration(5 * 60)
                    .username(savedUser.getUsername())
                    .tokenType(TokenType.Bearer)
                    .build();
        } catch (Exception e) {
            log.info("Error while creating user: " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while creating user"
            );
        }
    }

    /**
     * Sets the refresh token as a cookie.
     *
     * @param httpServletResponse The http response object.
     * @param refreshToken The refresh token.
     */
    private void setRefreshTokenCookie(HttpServletResponse httpServletResponse, String refreshToken) {
        log.info("Setting refresh token cookie");
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60 ); // 7 days
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setHttpOnly(true);
        httpServletResponse.addCookie(refreshTokenCookie);
        log.info("Refresh token cookie set");
    }

    /**
     * Gets a new access token from a refresh token.
     *
     * @param authorizationHeader The authorization header containing the refresh token.
     * @return AuthenticationResponseDTO containing the access token.
     */
    public AuthenticationResponseDTO getAccessTokenFromRefreshToken(String authorizationHeader) {
        if(!authorizationHeader.startsWith("Bearer ")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No bearer token found in header");
        }

        final String refreshToken = authorizationHeader.substring(7);

        RefreshToken refreshTokenDatabaseEntry = refreshTokenRepository.findByToken(refreshToken)
                .filter(token -> !token.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        User user = refreshTokenDatabaseEntry.getUser();
        Authentication authentication =  createAuthenticationObject(user);

        String accessToken = jwtTokenGenerationService.generateToken(user.getEmail());

        return  AuthenticationResponseDTO.builder()
                .token(accessToken)
                .tokenExpiration(15 * 60)
                .username(user.getEmail())
                .tokenType(TokenType.Bearer)
                .build();
    }

    /**
     * Creates an authentication object from a user object. Uses email as authentication username, because the email is
     * unique.
     *
     * @param user The user object.
     * @return The authentication object.
     */
    private static Authentication createAuthenticationObject(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        return new UsernamePasswordAuthenticationToken(
                email, password, List.of(new SimpleGrantedAuthority("USER"))
        );
    }
}
