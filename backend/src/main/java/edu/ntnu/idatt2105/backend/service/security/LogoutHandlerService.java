package edu.ntnu.idatt2105.backend.service.security;

import edu.ntnu.idatt2105.backend.model.users.RefreshToken;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.RefreshTokenRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class handles the logout process.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.0 26.03.2024
 */
@Service
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    /**
     * Revokes your refresh token when logging out.
     *
     * @param request The http request
     * @param response The http response
     * @param authentication The authentication object
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if(user.isEmpty())
            throw new RuntimeException("User not found");

        List<RefreshToken> tokens = refreshTokenRepository.findByUser(user.get());

        tokens.forEach(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }
}
