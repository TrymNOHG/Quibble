package edu.ntnu.idatt2105.backend.service.users;

import edu.ntnu.idatt2105.backend.config.UserConfig;
import edu.ntnu.idatt2105.backend.controller.priv.users.UserController;
import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserUpdateDTO;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for getting user information from their email.
 *
 * @author Trym Hamer Gudvangen, Brage Kvamme
 * @version 1.0 26.03.2024
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

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
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User " + email + " not found")
        );
    }


    /**
     * Update user information.
     *
     * @param userUpdateDTO The updated user information.
     * @return The updated user.
     */
    @Transactional
    public UserLoadDTO updateUser(UserUpdateDTO userUpdateDTO) {
        LOGGER.info(String.format("%s wants to update.", userUpdateDTO));
        // Check that user is updating self. Need userId from authentication...
        Long userId = 1L;
        LOGGER.info("User has been successfully updated");
        return null;
    }

    /**
     * Delete a user.
     *
     * @param userId The id of the user.
     */
    public void deleteUser(Long userId) {
        // TODO: Check that user is actually user.
        userRepository.deleteById(userId);
    }

}
