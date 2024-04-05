package edu.ntnu.idatt2105.backend.controller.pub.authentication;

import edu.ntnu.idatt2105.backend.dto.users.UserRegisterDTO;
import edu.ntnu.idatt2105.backend.dto.security.AuthenticationResponseDTO;
import edu.ntnu.idatt2105.backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.logging.Logger;

/**
 * This controller provides the public endpoint for authentication. This controller is used for
 * login, signup and retrieval of access tokens from refresh tokens.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.0 24.03.2024
 */
@RequestMapping("/api/v1/public/auth")
@RestController
@RequiredArgsConstructor // allow credentials yes
@CrossOrigin("*")
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class AuthenticationController implements IAuthenticationController {

    private final AuthenticationService authenticationService;
    private final Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    @Override
    public ResponseEntity<AuthenticationResponseDTO> login(Authentication authentication, HttpServletResponse httpServletResponse) {
        logger.info("Starting Login Process.");
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.login(authentication, httpServletResponse);
        logger.info("Login Process Completed.");
        return ResponseEntity.ok().body(authenticationResponseDTO);
    }

    @Override
    public ResponseEntity<AuthenticationResponseDTO> signup(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam(name = "image", required = false) MultipartFile imageFile,
            HttpServletResponse httpServletResponse
    ) {
        logger.info("Calling signup endpoint");
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(username, password, email);

        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.registerUser(
                userRegisterDTO, httpServletResponse, imageFile
        );
        logger.info("Sign-up Process Completed.");
        return ResponseEntity.ok(authenticationResponseDTO);
    }

    @Override
    public ResponseEntity<AuthenticationResponseDTO> getAccessTokenFromRefreshToken(
            @CookieValue(value = "refresh_token", defaultValue = "") String refreshToken
    ){
        return ResponseEntity.ok(authenticationService.getAccessTokenFromRefreshToken(refreshToken));
    }
}
