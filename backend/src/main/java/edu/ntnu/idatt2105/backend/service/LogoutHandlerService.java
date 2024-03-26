package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.dto.TokenType;
import edu.ntnu.idatt2105.backend.repo.users.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {

    private final RefreshTokenRepository refreshTokenRepository;

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

        refreshTokenRepository.findByToken(authHeader.substring(7))
                // This only revokes one token, a user can have multiple tokens
                .map(token->{token.setRevoked(true);
                    refreshTokenRepository.save(token);
                    return token;
                });
    }
}
