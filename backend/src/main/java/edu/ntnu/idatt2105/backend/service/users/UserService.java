package edu.ntnu.idatt2105.backend.service.users;

import edu.ntnu.idatt2105.backend.config.UserConfig;
import edu.ntnu.idatt2105.backend.controller.priv.users.UserController;
import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserUpdateDTO;
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
import java.net.MalformedURLException;

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
    private final UserMapper userMapper;
    private final ImageService imageService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username) // FindByEmail is used as the username in the authentication object is the email.
                .map(UserConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    public UserLoadDTO getUserByEmail(String email) throws UsernameNotFoundException {
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


    @Transactional
    public UserLoadDTO updateUser(UserUpdateDTO userUpdateDTO) {
        LOGGER.info(String.format("%s wants to update.", userUpdateDTO));
        // Check that user is updating self. Need userId from authentication...
        Long userId = 1L;
        LOGGER.info("User has been successfully updated");
        return null;
    }

    public void deleteUser(Long userId) throws IOException {
        // TODO: Check that user is actually user.
        userRepository.deleteById(userId);
        imageService.removeUserDir(userId);
    }

}
