package edu.ntnu.idatt2105.backend.controller.priv.users;

import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserUpdateDTO;
import edu.ntnu.idatt2105.backend.service.security.AuthenticationService;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import edu.ntnu.idatt2105.backend.service.users.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * This controller provides the private endpoint for users.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@RestController("privateUserController")
@EnableAutoConfiguration
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping(value = "/api/v1/private/users")
public class UserController implements IUserController{

    private final UserService userService;
    private final ImageService imageService;
    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<UserLoadDTO> updateUser(
            @NotNull UserUpdateDTO userUpdateDTO, @NotNull Authentication authentication
    ) throws IOException {
        UserLoadDTO userLoadDTO = userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok(userLoadDTO);
    }

    @Override
    public ResponseEntity<String> deleteProfilePicture(@NonNull Authentication authentication) throws IOException {
        imageService.setDefaultProfilePic(authenticationService.getLoggedInUserId());
        return ResponseEntity.ok("Profile picture deleted.");
    }

    @Override
    public ResponseEntity<String> deleteUser(Authentication authentication) {
        //TODO: fill in.
        return null;
    }

    @Override
    public ResponseEntity<Object> getUser(Authentication authentication) {
        UserLoadDTO userLoadDTO = userService.getUserLoadDTOByEmail(authentication.getName());
        return ResponseEntity.ok(userLoadDTO);
    }

//    @Override
//    public ResponseEntity<Object> updateUserShowActivity(boolean newShowActivity, @NonNull Authentication authentication) throws FileSystemException {
//        log.info("qwerqwer");
//        userService.updateUser(UserUpdateDTO.builder().showActivity(newShowActivity).build());
//        return ResponseEntity.ok("User Updated.");
//    }
//
//    @Override
//    public ResponseEntity<Object> updateUserShowFeedback(boolean newShowFeedback, @NonNull Authentication authentication) throws FileSystemException {
//        userService.updateUser(UserUpdateDTO.builder().showActivity(newShowFeedback).build());
//        return ResponseEntity.ok("User Updated.");
//    }


    // Update, delete

    // CRUD

}
