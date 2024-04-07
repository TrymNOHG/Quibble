package edu.ntnu.idatt2105.backend.config.filters;

import edu.ntnu.idatt2105.backend.service.security.JWTTokenService;
import edu.ntnu.idatt2105.backend.dto.security.RSAKeyPairDTO;
import edu.ntnu.idatt2105.backend.service.security.LogoutHandlerService;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import lombok.RequiredArgsConstructor;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


/**
 * Configures the security filter chain for the application. This application has a multitude of filter for different
 * endpoints. The filters are configured in a chain of filters.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.1 26.03.2024
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserService userService;
    private final RSAKeyPairDTO rsaKeyPairDTO;
    private final JWTTokenService jwtTokenService;
    private final LogoutHandlerService logoutHandlerService;

    private final String API_ENDPOINT_STRING = "/api/v1";
    private final String PUBLIC = "/public";
    private final String PRIVATE = "/private";

    /**
     * A common filter for the security filter chain.
     *
     * @param httpSecurity The httpSecurity object to configure.
     * @param antPattern The ant pattern to match the filter.
     * @return The httpSecurity object with the common configuration.
     * @throws Exception If an error occurs during configuration.
     */
    private HttpSecurity commonHttpSecurity(HttpSecurity httpSecurity, String antPattern) throws Exception {
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher(API_ENDPOINT_STRING + antPattern))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                });
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Auth-Token"));
        configuration.setExposedHeaders(List.of("X-Auth-Token"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.setAllowedOriginPatterns(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configures the first chain of the security filter chain.
     *
     * @param httpSecurity The httpSecurity object to configure.
     * @return The security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain loginFilter(HttpSecurity httpSecurity) throws Exception {
        return commonHttpSecurity(httpSecurity, PUBLIC+"/auth/login/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .userDetailsService(userService)
                .httpBasic(withDefaults())
                .build();
    }

    /**
      * Configures the second chain of the security filter chain. This chain is responsible for
      * handling all requests to the /api/** endpoints. If a private request is made, the user
      * must be authenticated with a token.
      *
      * @param httpSecurity The httpSecurity object to configure.
      * @return The security filter chain.
      * @throws Exception If an error occurs during configuration.
      */
    @Bean
    @Order(2)
    public SecurityFilterChain apiFilter(HttpSecurity httpSecurity) throws Exception{
        return commonHttpSecurity(httpSecurity, PRIVATE + "/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .addFilterBefore(
                        new JWTFilterChain(jwtTokenService, rsaKeyPairDTO),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    /**
     * Configures the third chain of the security filter chain. This chain is responsible for
     * handling all requests to the /refresh-token/** endpoints. If a refresh token request is made,
     * the user must be authenticated with a token.
     *
     * @param httpSecurity The httpSecurity object to configure.
     * @return The security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    @Order(3)
    public SecurityFilterChain refreshTokenFilter(HttpSecurity httpSecurity) throws Exception{
        return commonHttpSecurity(httpSecurity, PUBLIC+"/auth/refresh-token/**")
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .build();
    }

    /**
     * Filter for logging out. This filter is responsible for logging out the user.
     * It revokes the refresh token and clears the security context.
     * The user must be authenticated to log out.
     *
     * @param httpSecurity The httpSecurity object to configure.
     * @return The security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    @Order(4)
    public SecurityFilterChain logoutFilter(HttpSecurity httpSecurity) throws Exception {
        return commonHttpSecurity(httpSecurity, PUBLIC+"/auth/logout/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .logout(logout -> logout
                        .logoutUrl(API_ENDPOINT_STRING + PUBLIC + "/auth/logout")
                        .addLogoutHandler(logoutHandlerService)
                        .logoutSuccessHandler(
                                ((request, response, authentication) -> SecurityContextHolder.clearContext())
                        )
                )
                .build();
    }

    /**
     * Filter for signing up. This filter is responsible for signing up a user.
     * The user must be authenticated to sign up.
     *
     * @param httpSecurity The httpSecurity object to configure.
     * @return The security filter chain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    @Order(5)
    public SecurityFilterChain registerFilter(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher(API_ENDPOINT_STRING+PUBLIC+"/auth/signup/**"))
                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                .formLogin(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    /**
     * Configures the password encoder for the application.
     *
     * @return The password encoder.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the JWT encoder for the application.
     *
     * @return The JWT encoder.
     */
    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKeyPairDTO.rsaPublicKey())
                .privateKey(rsaKeyPairDTO.rsaPrivateKey())
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * Configures the JWT decoder for the application.
     *
     * @return The JWT decoder.
     */
    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeyPairDTO.rsaPublicKey()).build();
    }

}