package edu.ntnu.idatt2105.backend.controller.pub.authentication;

import edu.ntnu.idatt2105.backend.dto.users.UserRegisterDTO;
import edu.ntnu.idatt2105.backend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.List;
import java.util.logging.Logger;

/**
 * This controller provides the public endpoint for authentication. This controller is used for
 * login, signup and retrieval of access tokens from refresh tokens.
 *
 * @author brage
 * @version 1.0 24.03.2024
 */
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/public/auth")
public class AuthenticationController implements IAuthenticationController {

    private final AuthenticationService authenticationService;
    private final Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    /**
     * Endpoint for logging in. This endpoint returns the access and refresh token. The access token is returned
     * in the response body and the refresh token is returned as a cookie.
     *
     * @param authentication The authentication object
     * @param httpServletResponse The http response
     * @return The access token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(Authentication authentication, HttpServletResponse httpServletResponse) {
        logger.info("Starting login process.");
        return ResponseEntity.ok(authenticationService.getTokensFromAuth(authentication, httpServletResponse));
    }

    /**
     * Endpoint for signing up. This endpoint registers a new user and returns the access and refresh token.
     * The access token is returned in the response body and the refresh token is returned as a cookie.
     *
     * @param userRegisterDto The user register dto
     * @param httpServletResponse The http response
     * @param bindingResult The binding result
     * @return The access token
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDto,
                                          HttpServletResponse httpServletResponse, BindingResult bindingResult){
        logger.info("Calls signup endpoint");
        if (bindingResult.hasErrors()) {
            List<String> e = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e);
        }
        return ResponseEntity.ok(authenticationService.registerUser(userRegisterDto,httpServletResponse));
    }

    /**
     * Endpoint for refreshing the access token. This endpoint returns a new access token.
     *
     * @param authorizationHeader The authorization header
     * @return The new access token
     */
    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessTokenFromRefreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authenticationService.getAccessTokenFromRefreshToken(authorizationHeader));
    }
}
