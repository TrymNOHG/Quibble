package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.dto.AuthenticationResponseDTO;
import edu.ntnu.idatt2105.backend.dto.TokenType;
import edu.ntnu.idatt2105.backend.dto.UserRegisterDTO;
import edu.ntnu.idatt2105.backend.model.users.RefreshToken;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.RefreshTokenRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
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
import java.util.Optional;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JWTTokenService jwtTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    private final Logger log = Logger.getLogger(AuthenticationService.class.getName());

    public AuthenticationResponseDTO getTokensFromAuth(Authentication authentication, HttpServletResponse httpServletResponse) {
        try {
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                    ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
            );

            // Generates and saves refresh token
            String refreshToken = jwtTokenService.generateRefreshToken(authentication);
            RefreshToken refreshTokenEntity = RefreshToken.builder()
                    .user(user)
                    .token(refreshToken)
                    .revoked(false)
                    .build();
            refreshTokenRepository.save(refreshTokenEntity);

            setRefreshTokenCookie(httpServletResponse, refreshToken);

            return AuthenticationResponseDTO.builder()
                    .token(jwtTokenService.generateToken(authentication))
                    .tokenExpiration(60*15)
                    .username(user.getUsername())
                    .tokenType(TokenType.Bearer).build();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong: ", e); // Might be bad to show the user the error message for security reasons.
        }
    }

    public AuthenticationResponseDTO registerUser(UserRegisterDTO userRegistrationDto,
                                                  HttpServletResponse httpServletResponse) {
        try {
            if (userRegistrationDto.username().length() < 3 || userRegistrationDto.username().length() > 64
                    || userRegistrationDto.password().length() < 8 || userRegistrationDto.password().length() > 64) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password is invalid");
            }

            if (!userRegistrationDto.email().matches("^(.+)@(.+)$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
            }

            String profilePicLink = userRegistrationDto.profilePicLink();
            if (userRegistrationDto.profilePicLink() != null && !userRegistrationDto.profilePicLink().matches("^(http|https)://.*$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid profile picture link");
            } else {
                profilePicLink = "https://pbs.twimg.com/profile_images/740827537329229825/nAnThbbi_400x400.jpg";
            }

            log.info("Registering user with email: " + userRegistrationDto.email());
            Optional<User> user = userRepository.findByEmail(userRegistrationDto.email());
            if (user.isPresent() || userRepository.findByUsername(userRegistrationDto.username()).isPresent()
                    || userRepository.findByEmail(userRegistrationDto.email()).isPresent()) {
                log.info("User already exists");
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User Already Exist"); // TODO: fix this exception
            }

            User newUser = User.builder()
                    .email(userRegistrationDto.email())
                    .username(userRegistrationDto.username())
                    .password(passwordEncoder.encode(userRegistrationDto.password()))
                    .profilePicLink(profilePicLink) // TODO: Nå kan man legge inn hva som helst av linker. Er det sketchy?
                    .build();
            Authentication authentication = createAuthenticationObject(newUser);

            String accessToken = jwtTokenService.generateToken(authentication);
            String refreshToken = jwtTokenService.generateRefreshToken(authentication);

            newUser = userRepository.save(newUser);
            RefreshToken refreshTokenEntity = RefreshToken.builder()
                    .user(newUser)
                    .token(refreshToken)
                    .revoked(false)
                    .build();
            refreshTokenRepository.save(refreshTokenEntity);

            setRefreshTokenCookie(httpServletResponse, refreshToken);

            return AuthenticationResponseDTO.builder()
                    .token(accessToken)
                    .tokenExpiration(5 * 60)
                    .username(newUser.getUsername())
                    .tokenType(TokenType.Bearer)
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while creating user: ");
        }
    }

    private void setRefreshTokenCookie(HttpServletResponse httpServletResponse, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60 ); // 7 days
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setHttpOnly(true);
        httpServletResponse.addCookie(refreshTokenCookie);
    }

    public Object getAccessTokenFromRefreshToken(String authorizationHeader) {
        if(!authorizationHeader.startsWith("Bearer ")){
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "No bearer token found in header");
        }

        final String refreshToken = authorizationHeader.substring(7);

        RefreshToken refreshTokenDatabaseEntry = refreshTokenRepository.findByToken(refreshToken)
                .filter(token -> !token.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        User user = refreshTokenDatabaseEntry.getUser();
        Authentication authentication =  createAuthenticationObject(user);

        String accessToken = jwtTokenService.generateToken(authentication);

        return  AuthenticationResponseDTO.builder()
                .token(accessToken)
                .tokenExpiration(15 * 60)
                .username(user.getUsername())
                .tokenType(TokenType.Bearer)
                .build();
    }

    private static Authentication createAuthenticationObject(User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        return new UsernamePasswordAuthenticationToken(
                username, password, List.of(new SimpleGrantedAuthority("USER"))
        );
    }
}
