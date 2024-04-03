package edu.ntnu.idatt2105.backend.dto.users;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

/**
 * This record provides a data transfer object of updating a user.
 * @param userId            The id of the user, required.
 * @param username          The new username of the user.
 * @param profilePicture    The new profilePicture of the user.
 * @param showActivity      The new showActivity status.
 * @param showFeedback      The new showFeedback status.
 */
@Builder
public record UserUpdateDTO(@NonNull Long userId, @Nullable String username, @Nullable MultipartFile profilePicture,
                            @Nullable Boolean showActivity, @Nullable Boolean showFeedback) {
}
