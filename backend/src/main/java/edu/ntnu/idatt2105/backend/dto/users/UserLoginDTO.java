package edu.ntnu.idatt2105.backend.dto.users;

import lombok.Builder;
import lombok.NonNull;

/**
 * This record provides a data transfer object for a user logging in. Since the user can login with either email or
 * username, the first field is ambiguous.
 * @param emailOrUserName   Email or username of the user.
 * @param password          User's hashed and salted password.
 */
@Builder
public record UserLoginDTO(@NonNull String emailOrUserName, @NonNull String password) {
}
