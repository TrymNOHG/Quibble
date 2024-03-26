package edu.ntnu.idatt2105.backend.config.filters;

// TODO: Dette er kok

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import edu.ntnu.idatt2105.backend.config.JWTTokenComponent;
import edu.ntnu.idatt2105.backend.dto.RSAKeyPairDTO;
import edu.ntnu.idatt2105.backend.repo.users.RefreshTokenRepository;
import edu.ntnu.idatt2105.backend.service.LogoutHandlerService;
import edu.ntnu.idatt2105.backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserService userService;
    private final RSAKeyPairDTO rsaKeyPairDTO;
    private final JWTTokenComponent jwtTokenComponent;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LogoutHandlerService logoutHandlerService;

    /**
     * Configures the first chain of the security filter chain.
     *
     * @param httpSecurity The httpSecurity object to configure.
     * @return The security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain loginFilter(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/login/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .userDetailsService(userService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        ex -> {
                            ex.authenticationEntryPoint((request, response, authException) ->
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
                })
                .httpBasic(withDefaults())
                .build();
    }

    /**
      * Configures the second chain of the security filter chain. This chain is responsible for
      * handling all requests to the /api/** endpoints.
      *
      * @param httpSecurity The httpSecurity object to configure.
      * @return The security filter chain.
      * @throws Exception If an error occurs during configuration.
      */
    @Bean
    @Order(2)
    public SecurityFilterChain apiFilter(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> {
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                })
                .httpBasic(withDefaults())
                .securityMatcher(new AntPathRequestMatcher("/api/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JWTFilterChain(jwtTokenComponent, rsaKeyPairDTO), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Forandre rekkefølge
    @Bean
    @Order(3)
    public SecurityFilterChain refreshTokenFilter(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/refresh-token/**"))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> {
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                })
                .addFilterBefore(new JWTRefreshFilterChain(
                        rsaKeyPairDTO, refreshTokenRepository, jwtTokenComponent
                ), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain logoutFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/logout/**"))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandlerService)
                        .logoutSuccessHandler(
                                ((request, response, authentication) -> SecurityContextHolder.clearContext())
                        )
                )
                .addFilterBefore(new JWTFilterChain(jwtTokenComponent, rsaKeyPairDTO), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                })
                .build();
    }

    @Bean
    @Order(5)
    public SecurityFilterChain registerFilter(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/sign-up/**"))
                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKeyPairDTO.rsaPublicKey())
                .privateKey(rsaKeyPairDTO.rsaPrivateKey())
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeyPairDTO.rsaPublicKey()).build();
    }

}