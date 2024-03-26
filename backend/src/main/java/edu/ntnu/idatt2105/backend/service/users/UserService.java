package edu.ntnu.idatt2105.backend.service.users;

import edu.ntnu.idatt2105.backend.config.UserConfig;
import edu.ntnu.idatt2105.backend.controller.priv.users.UserController;
import edu.ntnu.idatt2105.backend.dto.users.UserLoadDTO;
import edu.ntnu.idatt2105.backend.dto.users.UserUpdateDTO;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username) // FindByEmail is used as the username in the authentication object is the email.
                .map(UserConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }


    @Transactional
    public UserLoadDTO updateUser(UserUpdateDTO userUpdateDTO) {
        LOGGER.info(String.format("%s wants to update.", userUpdateDTO));
        // Check that user is updating self. Need userId from authentication...
        Long userId = 1L;
        LOGGER.info("User has been successfully updated");
        return null;
    }

    public void deleteUser(Long userId) {
        // Check that user is actually user.
        userRepository.deleteById(userId);
    }

}
