package edu.ntnu.idatt2105.backend.controller.priv.images;

import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import edu.ntnu.idatt2105.backend.service.security.AuthenticationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;


/**
 * This controller provides the public endpoint for images.
 *
 * @author Trym Hamer Gudvangen
 * @author Brage Halvorsen Kvamme
 * @version 1.1 06.03.2024
 */
@RestController(value = "privateImageController")
@EnableAutoConfiguration
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping("/api/v1/private/image")
public class ImageController implements IImageController {

    private final ImageService imageService;
    private final AuthenticationService authenticationService;
    private final QuizService quizService;


    @Override
    public ResponseEntity<String> saveFile(
            @RequestParam("quizId") Long quizId,
            @RequestParam(name = "image", required = false) MultipartFile imageFile,
            @NonNull Authentication authentication
            ) {
        try {
            log.info("Getting image");
            Quiz quiz = quizService.getQuizById(quizId);
            User user = authenticationService.getLoggedInUser();
            if (!quiz.isCollaboratorOrAdmin(user)) {
                throw new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,
                        "Not the owner of the quiz.");
            }
            imageService.saveQuizImage(imageFile, quizId);
            return ResponseEntity.ok("Image saved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}