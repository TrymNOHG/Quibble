package edu.ntnu.idatt2105.backend.controller.pub.authentication;

import edu.ntnu.idatt2105.backend.dto.security.AuthenticationResponseDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserRegisterDTO;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully logged in"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @Operation(
            summary = "Login the user with username and password",
            description = """
                    Logs in a user and returns the access and refresh token. Send the username and password in the
                    request body as basic access authentication.
                    
                    Here is a resource on the basic access authentication:
                    
                    
                    https://en.wikipedia.org/wiki/Basic_access_authentication#:~:text=In%20basic%20HTTP%20authentication%2C%20a,HTTP%201.0%20specification%20in%201996

                    The response body contains the access token and the refresh token is returned as a cookie.
                    """,
            security = @SecurityRequirement(name = "basicAuth")
    )
    @PostMapping("/login")
    ResponseEntity<AuthenticationResponseDTO> login(
            Authentication authentication, HttpServletResponse httpServletResponse
    );

    /**
     * Endpoint for signing up. This endpoint registers a new user and returns the access and refresh token.
     * The access token is returned in the response body and the refresh token is returned as a cookie.
     *
     * @param userRegisterDTO The user register dto
     * @param httpServletResponse The http response
     * @param bindingResult The binding result
     * @return The access token
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User registered successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @Operation(summary = "Register a new user",
            description = """
                    Registers a new user and returns the access and refresh tokens. The access token is returned in the response body, and the refresh token is returned as a cookie. Provide user registration details in the request body.
                    """)
    @PostMapping("/signup")
    ResponseEntity<AuthenticationResponseDTO> signup(
            @Valid @RequestBody UserRegisterDTO userRegisterDTO,
            HttpServletResponse httpServletResponse, BindingResult bindingResult
    );

    /**
     * Endpoint for refreshing the access token. This endpoint returns a new access token.
     *
     * @param authorizationHeader The authorization header
     * @return The new access token
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Access token refreshed successfully"),
            @ApiResponse(code = 401, message = "Unauthorized, invalid refresh token")
    })
    @Operation(summary = "Refresh the access token",
            description = """
                    Refreshes the access token using the provided refresh token. Send the refresh token as an
                    Authorization header. The new access token is returned in the response body.
                    """)
    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    ResponseEntity<AuthenticationResponseDTO> getAccessTokenFromRefreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    );

}
