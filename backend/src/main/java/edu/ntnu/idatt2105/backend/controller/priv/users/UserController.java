package edu.ntnu.idatt2105.backend.controller.priv.users;

import edu.ntnu.idatt2105.backend.dto.users.UserUpdateDTO;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides the private endpoint for users.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@RestController
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/api/v1/private/users")
public class UserController implements IUserController{

    private final UserService userService;

    @Override
    public ResponseEntity<String> updateUser(UserUpdateDTO userUpdateDTO, Authentication authentication) {
        //TODO: fill in.
        return null;
    }

    @Override
    public ResponseEntity<String> deleteUser(Authentication authentication) {
        //TODO: fill in.
        return null;
    }

    // Update, delete

    // CRUD

}
