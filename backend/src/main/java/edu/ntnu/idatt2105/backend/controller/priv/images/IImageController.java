package edu.ntnu.idatt2105.backend.controller.priv.images;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageController {

        @PostMapping("/quiz/save")
        @Operation(summary = "This method saves an image.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Successfully saved image.",
                        content = @Content(mediaType = "image/jpeg", schema = @Schema(implementation = Resource.class))),
                @ApiResponse(responseCode = "403", description = "Not the owner of the quiz.")
        }
        )
        ResponseEntity<String> saveFile(
                @RequestParam("quizId") Long quizId,
                @RequestParam(name = "image", required = false) MultipartFile imageFile,
                @NonNull Authentication authentication
        ) throws IOException;

}
