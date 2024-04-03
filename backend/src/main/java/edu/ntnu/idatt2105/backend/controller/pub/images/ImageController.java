package edu.ntnu.idatt2105.backend.controller.pub.images;

import edu.ntnu.idatt2105.backend.dto.images.ImageLoadDTO;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This controller provides the public endpoint for images.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@RestController()
@EnableAutoConfiguration
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping(value = "/api/v1/public/image")
public class ImageController implements IImageController{

    private final ImageService imageService;

//    @Override
//    public ResponseEntity<Resource> getImage(@NonNull String userId, @NonNull String imagePath) {
//       log.info("Retrieving image with path : " + imagePath);
//       String fullPath = userId + "/" + imagePath;
//       Resource imageResource = imageService.loadImage(fullPath);
//       return ResponseEntity.ok().header(
//               HttpHeaders.CONTENT_DISPOSITION,
//               "attachment; filename=\"" + imageResource.getFilename() + "\""
//       ).body(imageResource);
//    }

//    @Override
//    public ResponseEntity<Resource> getImage(@NonNull String imagePath) {
//        log.info("Retrieving image with path : " + imagePath);
//        Resource imageResource = imageService.loadImage(imagePath);
//        return ResponseEntity.ok().header(
//                HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + imageResource.getFilename() + "\""
//        ).body(imageResource);
//    }

    @Override
    public ResponseEntity<ImageLoadDTO> getImage(@NonNull String userId, @NonNull String imagePath) throws IOException {
        log.info("Retrieving image with path : " + imagePath);
        String fullPath = userId + "/" + imagePath;
        ImageLoadDTO image = imageService.loadImage(fullPath);
        return ResponseEntity.ok().body(image);
    }

    @Override
    public ResponseEntity<ImageLoadDTO> getImage(@NonNull String imagePath) throws IOException {
        log.info("Retrieving image with path : " + imagePath);
        ImageLoadDTO image = imageService.loadImage(imagePath);
        return ResponseEntity.ok().body(image);
    }

}