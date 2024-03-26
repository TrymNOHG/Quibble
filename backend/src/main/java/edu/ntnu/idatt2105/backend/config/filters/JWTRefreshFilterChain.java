package edu.ntnu.idatt2105.backend.config.filters;

import edu.ntnu.idatt2105.backend.config.JWTTokenComponent;
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


@RequiredArgsConstructor
public class JWTRefreshFilterChain extends OncePerRequestFilter {

    private  final RSAKeyPairDTO rsaKeyPairDTO;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTTokenComponent jwtTokenComponent;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyPairDTO.rsaPublicKey()).build();

            if (!authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            Jwt jwtRefreshToken = jwtDecoder.decode(authHeader.substring(7));
            String userName = jwtTokenComponent.getUserName(jwtRefreshToken);


            if (SecurityContextHolder.getContext().getAuthentication() == null && !userName.isEmpty()) {
                boolean isRefreshTokenValidInDatabase = refreshTokenRepository
                        .findByToken(jwtRefreshToken.getTokenValue())
                        .map(refreshToken -> !refreshToken.isRevoked())
                        .orElse(false);

                UserDetails userDetails = jwtTokenComponent.getUserDetails(userName);
                if (jwtTokenComponent.isTokenValid(jwtRefreshToken, userDetails) && isRefreshTokenValidInDatabase) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}