package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.model.users.RefreshToken;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.RefreshTokenRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
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

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!authHeader.startsWith("Bearer "))
            return;

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
