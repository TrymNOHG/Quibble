package edu.ntnu.idatt2105.backend.controller.pub.images;

import edu.ntnu.idatt2105.backend.model.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface IImageController {

    // Retrieve images using link.


    /**
     * This method retrieves a photo from the server.
     *
     * @return ResponseEntity showing whether the operation was successful.
     */
    @GetMapping(
            value="/{imagePath}"
    )
    @Operation(summary = "This method retrieves a photo from the server.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of photo.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)) }),
    }
    )
    ResponseEntity<Resource> getImage(@PathVariable @NonNull String imagePath);


}
