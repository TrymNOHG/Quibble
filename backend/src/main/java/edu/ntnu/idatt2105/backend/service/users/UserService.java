package edu.ntnu.idatt2105.backend.service.users;

import edu.ntnu.idatt2105.backend.config.UserConfig;
import edu.ntnu.idatt2105.backend.dto.users.MultipleUserDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserUpdateDTO;
import edu.ntnu.idatt2105.backend.exception.exists.ExistsException;
import edu.ntnu.idatt2105.backend.mapper.users.UserMapper;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.Set;

/**
 * Service class for getting user information from their email.
 *
 * @author Trym Hamer Gudvangen
 * @author Brage Halvorsen Kvamme
 * @version 1.0 26.03.2024
 * @see User
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;

    /**
     * Load userDetals object by their email.
     *
     * @param email The email of the user.
     * @return The user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email) // FindByEmail is used as the username in the authentication object is the email.
                .map(UserConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
    }

    /**
     * Get user by their email.
     *
     * @param email The email of the user.
     * @return The user.
     */
    public User getUserByEmail(String email) throws UsernameNotFoundException {
        LOGGER.info("Attempting to retrieve user information.");
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
        LOGGER.info("Successful retrieval of user info.");
        return user;
    }

    /**
     * Get user by their email.
     *
     * @param email The email of the user.
     * @return The user.
     */
    public UserLoadDTO getUserLoadDTOByEmail(String email) throws UsernameNotFoundException {
        LOGGER.info("Attempting to retrieve user information.");
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
        LOGGER.info("Successful retrieval of user info.");
        LOGGER.info("Mapping User model to UserLoadDTO");
        UserLoadDTO userLoadDTO = userMapper.userToUserLoadDTO(user);
        LOGGER.info("Successful mapping.");
        return userLoadDTO;
    }

    /**
     * THis method gets a set of users based on a fuzzy search of their username.
     * @param fuzzyUsername     The input, username, to the fuzzy search algorithm
     * @return                  A multiple user DTO, containing a set of UserLoadDTO.
     */
    public MultipleUserDTO getUsersByUsernameFuzzy(String fuzzyUsername) {
        LOGGER.info("Looking for users that match the username: " + fuzzyUsername);
        Set<User> users = userRepository.findByUsernameContainingIgnoreCase(fuzzyUsername)
                .orElseThrow(() -> new UsernameNotFoundException(fuzzyUsername));

        LOGGER.info("Converting users to MultipleUserDTO");
        return userMapper.usersToMultipleUserLoadDTO(users);
    }


    /**
     * Update user information.
     *
     * @param userUpdateDTO The updated user information.
     * @return The updated user.
     */
    @Transactional
    public UserLoadDTO updateUser(UserUpdateDTO userUpdateDTO) throws FileSystemException {
        LOGGER.info(String.format("%s wants to update.", userUpdateDTO));
        // TODO: Check that user is updating self. Need userId from authentication...

        User user = userRepository.findById(userUpdateDTO.userId())
                .orElseThrow(() -> new UsernameNotFoundException("User id: " + userUpdateDTO.userId()));

        if(userUpdateDTO.username() != null) {
            if(userRepository.findByUsername(userUpdateDTO.username()).isPresent()) {
                LOGGER.warn("Username is already in-use.");
                throw new ExistsException("User", userUpdateDTO.username());
            }
            user.setUsername(userUpdateDTO.username());
        }

        if(userUpdateDTO.profilePicture() != null){
            String newProfilePicLink = imageService.saveImage(userUpdateDTO.profilePicture(), userUpdateDTO.userId());
            LOGGER.info("New Profile Pic Link: " + newProfilePicLink);
            user.setProfilePicLink(newProfilePicLink);
        }

        if(userUpdateDTO.showActivity() != null){
            user.setShowActivity(userUpdateDTO.showActivity());
        }

        if(userUpdateDTO.showFeedback() != null){
            user.setShowFeedback(userUpdateDTO.showFeedback());
        }

        userRepository.save(user);
        LOGGER.info("User has been successfully updated");
        return null;
    }

    /**
     * Delete a user.
     *
     * @param userId The id of the user.
     */
    public void deleteUser(Long userId) throws IOException {
        // TODO: Check that user is actually user.
        userRepository.deleteById(userId);
        imageService.removeUserDir(userId);
    }

}
