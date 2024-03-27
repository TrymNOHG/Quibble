package edu.ntnu.idatt2105.backend.config;

import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for inserting dummy data into the database on startup. If the data is already there, don't
 * insert it.
 *
 * @author brage
 * @version 1.0 26.03.2024
 */
@RequiredArgsConstructor
@Component
public class DummyData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = User.builder()
                .username("TestUser")
                .email("test@test.test")
                .password(passwordEncoder.encode("password"))
                .profilePicLink("https://www.google.com")
                .build();

        if (userRepository.findByUsername(user.getUsername()).isEmpty())
            userRepository.save(user);
    }
}
