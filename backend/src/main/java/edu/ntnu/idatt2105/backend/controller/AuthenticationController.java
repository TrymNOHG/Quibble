package edu.ntnu.idatt2105.backend.controller;

import edu.ntnu.idatt2105.backend.dto.UserRegisterDTO;
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

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    @PostMapping("/login")
    public ResponseEntity<?> login(Authentication authentication, HttpServletResponse httpServletResponse) {
        return ResponseEntity.ok(authenticationService.getTokensFromAuth(authentication, httpServletResponse));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDto,
                                          HttpServletResponse httpServletResponse, BindingResult bindingResult){
        logger.info("Calles dingup endpoint");
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

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')") // TODO: Scope might not work
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessTokenFromRefreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authenticationService.getAccessTokenFromRefreshToken(authorizationHeader));
    }
}
