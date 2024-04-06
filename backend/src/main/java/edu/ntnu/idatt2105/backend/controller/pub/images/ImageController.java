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
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


/**
 * This controller provides the public endpoint for images.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@RestController(value = "publicImageController")
@EnableAutoConfiguration
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
@RequestMapping("/api/v1/public/image")
public class ImageController implements IImageController {

    private final ImageService imageService;

    @Override
    public ResponseEntity<Resource> getFile(@PathVariable String image) {
        try {
            log.info("Getting image");
            return imageService.getFile(image);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}