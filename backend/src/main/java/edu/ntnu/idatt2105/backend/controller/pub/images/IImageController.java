package edu.ntnu.idatt2105.backend.controller.pub.images;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

/**
 * This interface outlines the public endpoints related to images.
 *
 * @author Brage Halvorsen Kvamme
 * @version 1.1 06.04.2024
 */
public interface IImageController {

        @GetMapping("/{image}")
        @Operation(summary = "This method gets the image.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Successfully retrieved image.",
                        content = @Content(mediaType = "image/jpeg", schema = @Schema(implementation = Resource.class))),
                @ApiResponse(responseCode = "404", description = "Image not found.")
        }
        )
        ResponseEntity<Resource> getFile(@PathVariable String image) throws IOException;

}
