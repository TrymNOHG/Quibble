package edu.ntnu.idatt2105.backend.controller.pub.authentication;

import edu.ntnu.idatt2105.backend.dto.AuthenticationResponseDTO;
import edu.ntnu.idatt2105.backend.dto.UserRegisterDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * This interface contains the methods of the AuthenticationController.
 */
public interface IAuthenticationController {

    /**
     * Endpoint for logging in. This endpoint returns the access and refresh token. The access token is returned
     * in the response body and the refresh token is returned as a cookie.
     *
     * @param authentication The authentication object
     * @param httpServletResponse The http response
     * @return The access token
     */
    public ResponseEntity<AuthenticationResponseDTO> login(
            Authentication authentication, HttpServletResponse httpServletResponse
    );

    /**
     * Endpoint for signing up. This endpoint registers a new user and returns the access and refresh token.
     * The access token is returned in the response body and the refresh token is returned as a cookie.
     *
     * @param userRegisterDto The user register dto
     * @param httpServletResponse The http response
     * @param bindingResult The binding result
     * @return The access token
     */
    public ResponseEntity<AuthenticationResponseDTO> signup(
            @Valid @RequestBody UserRegisterDTO userRegisterDto,
            HttpServletResponse httpServletResponse, BindingResult bindingResult
    );

    /**
     * Endpoint for refreshing the access token. This endpoint returns a new access token.
     *
     * @param authorizationHeader The authorization header
     * @return The new access token
     */
    public ResponseEntity<AuthenticationResponseDTO> getAccessTokenFromRefreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );
}
