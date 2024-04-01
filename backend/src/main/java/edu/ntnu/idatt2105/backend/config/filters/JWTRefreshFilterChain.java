package edu.ntnu.idatt2105.backend.config.filters;

import edu.ntnu.idatt2105.backend.service.JWTTokenService;
import edu.ntnu.idatt2105.backend.dto.security.RSAKeyPairDTO;
import edu.ntnu.idatt2105.backend.repo.users.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filer chain for the refresh token. This filter checks if the refresh token is valid and if
 * the user is in the database.
 * If the refresh token is valid, it sets the security context with the user details.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.1 29.03.2024
 */
@RequiredArgsConstructor
public class JWTRefreshFilterChain extends OncePerRequestFilter {

    private  final RSAKeyPairDTO rsaKeyPairDTO;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTTokenService jwtTokenService;
    private final Logger log = Logger.getLogger(JWTRefreshFilterChain.class.getName());


    /**
     * Filters the request and checks if the refresh token is valid.
     * If the refresh token is valid, it sets the security context with the user details.
     *
     * @param request The http request
     * @param response The http response
     * @param filterChain The filter chain
     * @throws ServletException Exception if the request could not be handled
     * @throws IOException Exception if the request could not be handled
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Filtering request for refresh token.");
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyPairDTO.rsaPublicKey()).build();

            if (!authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            Jwt jwtRefreshToken = jwtDecoder.decode(authHeader.substring(7));
            String email = jwtTokenService.getUserEmail(jwtRefreshToken);
            log.info("Email from refresh token: " + email);

            UserDetails userDetails = jwtTokenService.getUserDetails(email);
            if (userDetails == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null && !email.isEmpty()) {
                boolean isRefreshTokenValidInDatabase = refreshTokenRepository
                        .findByToken(jwtRefreshToken.getTokenValue())
                        .map(refreshToken -> !refreshToken.isRevoked())
                        .orElse(false);
                log.info("Is refresh token valid in database: " + isRefreshTokenValidInDatabase);
                log.info("Is token valid: " + jwtTokenService.isValidToken(jwtRefreshToken, userDetails));
                log.info("Userdetails username "+userDetails.getUsername());

                if (jwtTokenService.isValidToken(jwtRefreshToken, userDetails) && isRefreshTokenValidInDatabase) {
                    log.info("Setting security context with user details from refresh token.");
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(createdToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
            filterChain.doFilter(request, response);
        }catch (JwtValidationException jwtValidationException){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,jwtValidationException.getMessage());
        }
    }
}