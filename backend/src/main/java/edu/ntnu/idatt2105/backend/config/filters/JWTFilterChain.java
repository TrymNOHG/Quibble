package edu.ntnu.idatt2105.backend.config.filters;

import edu.ntnu.idatt2105.backend.service.JWTTokenService;
import edu.ntnu.idatt2105.backend.dto.security.RSAKeyPairDTO;
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
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filter that checks if the request has a valid JWT token.
 *
 * @version 1.1 29.03.2024
 * @author Brage Halvorsen Kvamme
 */
@RequiredArgsConstructor
public class JWTFilterChain extends OncePerRequestFilter {

    private final JWTTokenService jwtTokenService;
    private final RSAKeyPairDTO rsaKeyPairDTO;
    private final Logger log = Logger.getLogger(JWTFilterChain.class.getName());

    /**
     * Checks if the request has a valid JWT token. If it does, it sets the security context with the user details.
     * Also checks if the user is in the database. This prevents tokens from deleted users to work.
     *
     * @param request Data from the client.
     * @param response Data to send to the client.
     * @param filterChain The chain of filters to go through.
     * @throws ServletException If an error occurs during the filter process.
     * @throws IOException If an error occurs during the filter process.
     */
    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try{
            log.info("Filtering request");
            String authenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if(!authenticationHeader.startsWith("Bearer ")){ // If no token, go to next filter.
                log.info("No token found");
                filterChain.doFilter(request, response);
                return;
            }
            log.info("Token found");

            final String token = authenticationHeader.substring(7);
            final Jwt jwtToken = NimbusJwtDecoder.withPublicKey(rsaKeyPairDTO.rsaPublicKey()).build().decode(token);
            final String email = jwtTokenService.getUserEmail(jwtToken);

            if(!email.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = jwtTokenService.getUserDetails(email);
                if(jwtTokenService.isValidToken(jwtToken, userDetails)){

                    UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    newToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(newToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            }

            filterChain.doFilter(request,response);
        }catch (JwtValidationException jwtValidationException){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,jwtValidationException.getMessage());
        }
    }
}
