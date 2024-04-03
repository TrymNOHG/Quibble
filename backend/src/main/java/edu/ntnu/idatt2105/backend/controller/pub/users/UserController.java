package edu.ntnu.idatt2105.backend.controller.pub.users;

import edu.ntnu.idatt2105.backend.dto.users.MultipleUserDTO;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides the public endpoint for users.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@RestController("publicUserController")
@EnableAutoConfiguration
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping(value = "/api/v1/public/users")
public class UserController implements IUserController {
    private final UserService userService;

    @Override
    public ResponseEntity<MultipleUserDTO> findUsersByUsername(@NonNull String username) {
        return ResponseEntity.ok(userService.getUsersByUsernameFuzzy(username));
    }
}
