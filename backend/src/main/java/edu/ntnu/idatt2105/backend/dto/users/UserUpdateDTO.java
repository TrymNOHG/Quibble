package edu.ntnu.idatt2105.backend.dto.users;

import lombok.NonNull;
import org.springframework.lang.Nullable;

public record UserUpdateDTO(@NonNull Long userId, @Nullable String username, @Nullable byte[] profilePicture) {
}
