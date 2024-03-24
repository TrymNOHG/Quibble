package edu.ntnu.idatt2105.backend.mapper.users;

import edu.ntnu.idatt2105.backend.dto.users.MultipleUserDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.model.users.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * This class creates mapping between the User model and its respective DTOs.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    //TODO: create method to retrieve profilePicture from server.

    @Mapping(target = "profilePicture", source = "")
    UserLoadDTO userToUserLoadDTO(User user);

    // Make a mapper for DTO to user, when registering.

    @Mapping(target = "users", source = "users", qualifiedByName = "userToUserLoadDTO")
    MultipleUserDTO userToMultipleUserLoadDTO(List<User> users);

}
