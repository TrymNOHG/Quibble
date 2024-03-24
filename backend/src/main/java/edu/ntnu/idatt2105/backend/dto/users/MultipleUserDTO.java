package edu.ntnu.idatt2105.backend.dto.users;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

/**
 * This DTO allows multiple UserLoadDTOs to be sent. This can be useful for instances such as quiz authors.
 * @param users     The users being sent.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 23.03.2024
 */
@Builder
public record MultipleUserDTO(@NonNull List<UserLoadDTO> users) {
}
